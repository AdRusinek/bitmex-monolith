package com.rusinek.bitmexmonolith.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import com.rusinek.bitmexmonolith.exceptions.BitmexExceptionService;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.model.response.ApiKey;
import com.rusinek.bitmexmonolith.repositories.AccountRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rusinek.bitmexmonolith.services.ExchangeService.HTTP_METHOD.GET;
import static com.rusinek.bitmexmonolith.services.ExchangeService.HTTP_METHOD.POST;


/**
 * Created by Adrian Rusinek on 09.03.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final BitmexExceptionService bitmexExceptionService;
    private final RequestService requestService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Value("${bitmex-monolith.exchange-url}")
    private String exchangeUrl;
    private static final String basePath = "/api/v1";
    private static final int expireSeconds = 5;


    // returns 0 if limits exceeded
    // returns 1 if key does not have permission to place orders
    // returns 2 if all good
    // returns different status if BitMEX has a problem
    public int testConnection(String varPath, String apiKey, String apiKeySecret, String username) {
        // set api-expires
        String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + expireSeconds);

        // get signContent
        String path = basePath + varPath;
        String url = exchangeUrl + path;
        String signContent = GET.toString() + path + apiExpires;

        // set apiSignature
        String apiSignature = setApiSignature(apiKeySecret, signContent);

        // get headers
        HashMap<String, String> headers = setHeaders(apiKey, apiExpires, apiSignature);

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            if (user.get().getUserRequestLimit().getApiReadyToUse() <= System.currentTimeMillis() / 1000L) {
                try {
                    return requestApiForApiKey(url, headers, username, apiKey);
                } catch (UnirestException e) {
                    log.error("Error occurred while sending url.");
                    e.printStackTrace();
                    return 2;
                }
            }
        }
        return 0;
    }


    public Object requestApiWithPost(String varPath, Map<String, Object> params, Long id, String userName) {

        Optional<Account> account = accountRepository.findByAccountOwnerAndId(userName, id);

        if (account.isPresent()) {
            // set api-expires
            String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + expireSeconds);

            // get signContent
            String paramsEncodedStr = getEncodedStrOfParams(params);
            String path = basePath + varPath;
            String url = exchangeUrl + path;
            String signContent = POST.toString() + path + apiExpires + paramsEncodedStr;

            // set apiSignature
            String apiSignature = setApiSignature(account.get().getApiKeySecret(), signContent);

            // get headers
            HashMap<String, String> headers = setHeaders(account.get().getApiKey(), apiExpires, apiSignature);


            if (account.get().getAccountRequestLimit().getApiReadyToUse() <= System.currentTimeMillis() / 1000L) {
                try {
                    HttpResponse<String> response = requestApi(POST, url, headers);
                    requestService.manageAccountLimits(response, account.get());
                    return response;
                } catch (UnirestException e) {
                    log.error("Error occurred while sending url.");
                    e.printStackTrace();
                }
            }
            //returns empty object only if error occurred or limits exceeded
            return new Object();
        }
        return new Object();
    }

    HttpResponse<String> requestApiWithGet(String varPath, Long id, String userName) {

        Optional<Account> account = accountRepository.findByAccountOwnerAndId(userName, id);

        if (account.isPresent()) {

            // set api-expires
            String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + expireSeconds);

            // get signContent
            String path = basePath + varPath;
            String signContent = GET.toString() + path + apiExpires;
            String url = exchangeUrl + path;

            // set apiSignature
            String apiSignature = setApiSignature(account.get().getApiKeySecret(), signContent);

            // get headers
            HashMap<String, String> headers = setHeaders(account.get().getApiKey(), apiExpires, apiSignature);

            //check for limits
            if (account.get().getAccountRequestLimit().getApiReadyToUse() <= System.currentTimeMillis() / 1000L) {
                try {
                    HttpResponse<String> response = requestApi(GET, url, headers);
                    requestService.manageAccountLimits(response, account.get());
                    return response;
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private HttpResponse<String> requestApi(HTTP_METHOD method, String url, HashMap<String, String> headers) throws UnirestException {
        HttpResponse<String> response = null;
        ignoreCookies();
        if (method == GET) {
            response = Unirest.get(url)
                    .headers(headers)
                    .asString();

        } else if (method == POST) {
            response = Unirest.post(url)
                    .headers(headers)
                    .asString();
        }
        return response;
    }

    private int requestApiForApiKey(String url, HashMap<String, String> headers, String username, String apiKey) throws UnirestException {
        // getting response
        HttpResponse<String> response = requestApi(GET, url, headers);
        ObjectMapper objectMapper = new ObjectMapper();
        // checking if somebody is trying to many requests
        requestService.manageUserLimits(username);
        // casting to model object and excluding permissions list to check if it has order capability
        // if it is not there returns 1 which causing specific validation response
        List<ApiKey> apiKeys;
        try {
            // trying to get all api keys
            apiKeys = objectMapper.readValue(response.getBody(), new TypeReference<List<ApiKey>>() {
            });
        } catch (JsonProcessingException e) {
            bitmexExceptionService.processErrorResponse(objectMapper, response);
            return 2;
        }
        for (ApiKey key : apiKeys) {
            for (String permission : key.getPermissions()) {
                if (apiKey.equals(key.getId()) && !permission.equals("order")) {
                    return 1;
                }
                // if all went perfect it refreshes user limits to zero
                if (apiKey.equals(key.getId()) && permission.equals("order")) {
                    requestService.refreshUserLimits(username);
                }
            }
        }
        return response.getStatus();
    }

    private void ignoreCookies() {
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

        HttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        Unirest.setHttpClient(httpclient);
    }

    private HashMap<String, String> setHeaders(String apiKey, String apiExpires, String apiSignature) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("api-expires", apiExpires);
        headers.put("api-signature", apiSignature);

        return headers;
    }

    private String setApiSignature(String apiKeySecret, String signContent) {
        HashFunction hashFunc = Hashing.hmacSha256(apiKeySecret.getBytes(Charset.forName("UTF-8")));
        HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(Charset.forName("UTF-8")));
        return hashCode.toString();
    }

    private String getEncodedStrOfParams(Map<String, Object> params) {
        MultipartBody body = Unirest.post("").fields(params);
        try {
            InputStream bodyStream = body.getEntity().getContent();
            return IOUtils.toString(bodyStream, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum HTTP_METHOD {
        GET,
        POST,
    }
}
