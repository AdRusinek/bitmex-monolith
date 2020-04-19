package com.rusinek.bitmexmonolith.services.exchange;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountNotFoundException;
import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static com.rusinek.bitmexmonolith.services.exchange.ExchangeService.HTTP_METHOD.GET;
import static com.rusinek.bitmexmonolith.services.exchange.ExchangeService.HTTP_METHOD.POST;


/**
 * Created by Adrian Rusinek on 09.03.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final AccountRepository accountRepository;

    @SuppressWarnings("UnstableApiUsage")
    public Object requestApi(ExchangeService.HTTP_METHOD method, String varPath,
                             Map<String, Object> params, Long id, String userName) {

        final Account account;
        try {
            account = accountRepository.findByAccountOwnerAndId(userName, Long.valueOf(id));
            if (account == null) {
                log.debug("Element does not exist or does not belong to your account");
                throw new AccountNotFoundException("Element does not exist or does not belong to your account");
            }
        } catch (NumberFormatException ex) {
            throw new AccountNotFoundException("Element '" + id + "' can't be cast to type Long");
        }

        // set api-expires
        String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + 20);

        // get signContent
        String paramsEncodedStr = getEncodedStrOfParams(params);
        String path = "/api/v1" + varPath;
        if ((method == GET) && !paramsEncodedStr.isEmpty()) {
            path += "?" + paramsEncodedStr;
        }
        String url = "https://testnet.bitmex.com/" + path;
        String signContent = method.toString() + path + apiExpires;
        if (method == POST) {
            signContent += paramsEncodedStr;
        }

        // set apiSignature
        HashFunction hashFunc = Hashing.hmacSha256(account.getApiKeySecret().getBytes(Charset.forName("UTF-8")));
        HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(Charset.forName("UTF-8")));
        String apiSignature = hashCode.toString();

        // get headers
        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", account.getApiKey());
        headers.put("api-expires", apiExpires);
        headers.put("api-signature", apiSignature);

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
                return gson.fromJson(response.getBody(), Object.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
