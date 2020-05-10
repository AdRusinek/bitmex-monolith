package com.rusinek.bitmexmonolith.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import com.rusinek.bitmexmonolith.model.response.ApiKeyResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrian Rusinek on 07.05.2020
 **/
public class TestingPurposes {

    private static final String basePath = "/api/v1";
    private static final int expireSeconds = 5;
    private static final Charset charset = Charset.forName("UTF-8");
    private static final Gson gson = new Gson();
    private static TestingPurposes instance = null;
    private String host = null;
    private String apiKey = null;
    private String apiKeySecret = null;

    private TestingPurposes(String host, String apiKey, String apiKeySecret) {
        this.host = host;
        this.apiKey = apiKey;
        this.apiKeySecret = apiKeySecret;
    }

    public static TestingPurposes getInstance() {
        if (instance == null) {
            instance = testInstance();

            // This can be removed if you don't have the ssl connection problem
//            Unirest.setHttpClient(getUnsafeHttpClient());
        }
        return instance;
    }



    private static String getEncodedStrOfParams(Map<String, Object> params) {
        MultipartBody body = Unirest.post("")
                .fields(params);
        try {
            InputStream bodyStream = body.getEntity().getContent();
            return IOUtils.toString(bodyStream, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum HTTP_METHOD {
        GET,
        POST
    }

    public HttpResponse<?> requestApi(BitMexUtil.HTTP_METHOD method, String varPath, Map<String, Object> params) {

        // set api-expires
        long apiExpires = System.currentTimeMillis() / 1000 + expireSeconds;

        System.out.println(apiExpires);

        // get signContent
        String paramsEncodedStr = getEncodedStrOfParams(params);
        String path = basePath + varPath;
        if ((method == BitMexUtil.HTTP_METHOD.GET) && !paramsEncodedStr.equals("")) {
            path += "?" + paramsEncodedStr;
        }
        String url = host + path;
        String signContent = method.toString() + path + apiExpires;
        if (method == BitMexUtil.HTTP_METHOD.POST) {
            signContent += paramsEncodedStr;
        }

        // set apiSignature
        HashFunction hashFunc = Hashing.hmacSha256(apiKeySecret.getBytes(charset));
        HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(charset));
        String apiSignature = hashCode.toString();
        System.out.println("*********************************************");
        System.out.println(apiSignature);
        System.out.println("*********************************************");
        // get headers
        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("api-expires", String.valueOf(apiExpires));
        headers.put("api-signature", apiSignature);

        try {
            RequestConfig globalConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

            HttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
            Unirest.setHttpClient(httpclient);
            HttpResponse<String> response = null;

            if (method == BitMexUtil.HTTP_METHOD.GET) {

                response = Unirest.get(url)
                        .headers(headers)
                        .asString();

            } else if (method == BitMexUtil.HTTP_METHOD.POST) {
                response = Unirest.post(url)
                        .headers(headers)
                        .fields(params)
                        .asString();
            }
            return response;

//            return responseEntity;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) throws JsonProcessingException {

        TestingPurposes testingPurposes = TestingPurposes.getInstance();

        Map<String, Object> params = new HashMap<>();
//        Map<String, Object> filterMap = new HashMap<>();
//        filterMap.put("isOpen", true);
//        params.put("filter", gson.toJson(filterMap));
//        List<Map> positionList = null;
//
//        HttpResponse<?> response = testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/position", params);
//
//        System.out.println(response.getBody());

        HttpResponse<?> apiKeyResponse2 = testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/apiKey?reverse=false", params);
        System.out.println(apiKeyResponse2.getStatus());
        ObjectMapper m = new ObjectMapper();
        List<ApiKeyResponse> responses = m.readValue(apiKeyResponse2.getBody().toString(), new TypeReference<List<ApiKeyResponse>>() {
        });
        System.out.println(responses);

//        ApiKeyResponse apiKeyResponse  = (ApiKeyResponse) testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/apiKey?reverse=false", params).getBody();
//        System.out.println(apiKeyResponse);

    }
}
