package com.rusinek.bitmexmonolith.services;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.AccountRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
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


    private final RequestService requestService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Value("${bitmex-monolith.exchange-url}")
    private String exchangeUrl;
    private static final String basePath = "/api/v1";
    private static final int expireSeconds = 10;


    public int testConnection(ExchangeService.HTTP_METHOD method, String varPath, String apiKey, String apiKeySecret, String username) {

        log.debug("Testing connection");

        // set api-expires
        String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + expireSeconds);

        // get signContent
        String path = basePath + varPath;
        String url = exchangeUrl + path;

        String signContent = method.toString() + path + apiExpires;

        // set apiSignature
        HashFunction hashFunc = Hashing.hmacSha256(apiKeySecret.getBytes(Charset.forName("UTF-8")));
        HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(Charset.forName("UTF-8")));
        String apiSignature = hashCode.toString();

        // get headers
        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("api-expires", apiExpires);
        headers.put("api-signature", apiSignature);

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            if (user.get().getUserRequestLimit().getApiReadyToUse() <= System.currentTimeMillis() / 1000L) {
                try {
                    HttpResponse<String> response = null;
                    if (method == GET) {
                        response = Unirest.get(url)
                                .headers(headers)
                                .asString();

                    } else if (method == POST) {
                        response = Unirest.post(url)
                                .headers(headers)
                                .asString();
                    }
                    if (response != null) {
                        System.out.println("manage");
                        requestService.manageUserLimits(username);
                        return response.getStatus();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }


    public Object requestApi(ExchangeService.HTTP_METHOD method, String varPath,
                             Map<String, Object> params, Long id, String userName) {

        Optional<Account> account = accountRepository.findByAccountOwnerAndId(userName, id);

        if (account.isPresent()) {
            // set api-expires
            String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + 5);

            // get signContent
            String paramsEncodedStr = getEncodedStrOfParams(params);
            String path = "/api/v1" + varPath;
            if ((method == GET) && !paramsEncodedStr.isEmpty()) {
                path += "?" + paramsEncodedStr;
            }
            String url = exchangeUrl + path;
            String signContent = method.toString() + path + apiExpires;
            if (method == POST) {
                signContent += paramsEncodedStr;
            }

            // set apiSignature
            HashFunction hashFunc = Hashing.hmacSha256(account.get().getApiKeySecret().getBytes(Charset.forName("UTF-8")));
            HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(Charset.forName("UTF-8")));
            String apiSignature = hashCode.toString();

            // get headers
            Map<String, String> headers = new HashMap<>();
            headers.put("api-key", account.get().getApiKey());
            headers.put("api-expires", apiExpires);
            headers.put("api-signature", apiSignature);


            if (account.get().getAccountRequestLimit().getApiReadyToUse() <= System.currentTimeMillis() / 1000L) {
                try {
                    HttpResponse<String> response = null;
                    if (method == GET) {
                        response = Unirest.get(url)
                                .headers(headers)
                                .asString();

                    } else if (method == POST) {
                        response = Unirest.post(url)
                                .headers(headers)
                                .fields(params)
                                .asString();
                    }
                    if (response != null) {
                        Gson gson = new GsonBuilder().setLenient().create();
                        ResponseEntity errors = checkRequestForErrors(response);
                        if (errors != null) return errors;
                        requestService.manageAccountLimits(response, account.get());
                        return gson.fromJson(response.getBody(), Object.class);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return new Object();
                }
            }
            //returns empty object only if error occurred or limits exceeded
            return new Object();
        }
        return new Object();
    }

    public Object requestApi(ExchangeService.HTTP_METHOD method, String varPath, Long id, String userName) {

        Optional<Account> account = accountRepository.findByAccountOwnerAndId(userName, id);

        if (account.isPresent()) {
            // set api-expires
            String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + 300);

            // get signContent
            String path = basePath + varPath;
            String url = exchangeUrl + path;

            String signContent = method.toString() + path + apiExpires;

            // set apiSignature
            HashFunction hashFunc = Hashing.hmacSha256(account.get().getApiKeySecret().getBytes(Charset.forName("UTF-8")));
            HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(Charset.forName("UTF-8")));
            String apiSignature = hashCode.toString();

            // get headers
            Map<String, String> headers = new HashMap<>();
            headers.put("api-key", account.get().getApiKey());
            headers.put("api-expires", apiExpires);
            headers.put("api-signature", apiSignature);

            if (account.get().getAccountRequestLimit().getApiReadyToUse() <= System.currentTimeMillis() / 1000L) {
                try {
                    HttpResponse<String> response = null;
                    if (method == GET) {
                        response = Unirest.get(url)
                                .headers(headers)
                                .asString();

                    } else if (method == POST) {
                        response = Unirest.post(url)
                                .headers(headers)
                                .asString();
                    }
                    if (response != null) {
                        Gson gson = new GsonBuilder().setLenient().create();
                        ResponseEntity errors = checkRequestForErrors(response);
                        //todo sprawdzic czy to errors jest dobrze przekazywane do serwisow
                        if (errors != null) return errors;
                        requestService.manageAccountLimits(response, account.get());
                        return gson.fromJson(response.getBody(), Object.class);
                    }

                } catch (Exception e) {
                    log.error("Error occurred when calling BitMEX api [Stack trace:] " + Arrays.toString(e.getStackTrace()));
                }
            }
            //returns empty object only if error occurred or limits exceeded
            return new Object();
        }
        return new Object();
    }

    private ResponseEntity checkRequestForErrors(HttpResponse<String> response) {
        if (response.getBody().contains("error")) {
            log.error("Error occurred while requesting BitMEX API. Status code: '"
                    + response.getStatus() + "' with description: " + response.getBody());
            return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getStatus()));
        }
        return null;
    }


    private String getEncodedStrOfParams(Map<String, Object> params) {
        MultipartBody body = Unirest.post("")
                .fields(params);
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
