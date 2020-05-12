package com.rusinek.bitmexmonolith.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import com.rusinek.bitmexmonolith.model.response.Order;
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
import java.util.stream.Collectors;

import static com.rusinek.bitmexmonolith.services.exchange.ExchangeService.HTTP_METHOD.GET;

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

        HttpResponse<String> response = null;
        try {
            RequestConfig globalConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

            HttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
            Unirest.setHttpClient(httpclient);

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
            System.out.println("ops");
        }

        return null;
    }

    HttpResponse<String> requestApiWithGet(BitMexUtil.HTTP_METHOD method, String varPath) {


        // set api-expires
        String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + expireSeconds);

        // get signContent
        String path = basePath + varPath;
        String signContent = GET.toString() + path + apiExpires;
        String url = "https://testnet.bitmex.com/" + path;

        // set apiSignature
        HashFunction hashFunc = Hashing.hmacSha256(apiKeySecret.getBytes(Charset.forName("UTF-8")));
        HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(Charset.forName("UTF-8")));
        String apiSignature = hashCode.toString();

        // get headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("api-expires", apiExpires);
        headers.put("api-signature", apiSignature);

        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

        HttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        Unirest.setHttpClient(httpclient);

        try {
            HttpResponse<String> response = null;
            if (method == BitMexUtil.HTTP_METHOD.GET) {
                response = Unirest.get(url)
                        .headers(headers)
                        .asString();

            } else if (method == BitMexUtil.HTTP_METHOD.POST) {
                response = Unirest.post(url)
                        .headers(headers)
                        .asString();
            }
            return response;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Order> extractAndSetNewPrice(List<Order> orders) {
        return orders.stream().peek(order -> {
            if (order.getSide().equals("Sell")) {
                order.setOrderQty(-order.getOrderQty());
            }
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        TestingPurposes testingPurposes = TestingPurposes.getInstance();
//
//        HttpResponse<String> response = testingPurposes.requestApiWithGet(BitMexUtil.HTTP_METHOD.GET, OPENED_STOP_ORDERS);
//
//        List<Order> orders = null;
//        try {
//            orders = objectMapper.readValue(response.getBody(), new TypeReference<List<Order>>() {
//            });
//        } catch (JsonProcessingException e) {
//
//        }
//
//        System.out.println(orders);


//        orders.forEach(order -> {
//            System.out.println(order.getOrderQty());
//        });
//
//        testingPurposes.extractAndSetNewPrice(orders).forEach(order -> {
//            System.out.println(order.getOrderQty());
//        });
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("isOpen", true);
        filterMap.put("symbol", "XBTUSD");
        params.put("filter", gson.toJson(filterMap));
        HttpResponse<?> responsePosition = testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/position", params);
        System.out.println(responsePosition.getBody());

        params.clear();
        filterMap.clear();

        filterMap.put("symbol", "XBTUSD");
        filterMap.put("ordType", "Limit");
        filterMap.put("open", true);

        params.put("symbol", "XBT");
        params.put("filter", gson.toJson(filterMap));
        params.put("count", 20);
        params.put("reverse", false);

        HttpResponse<?> responseOrderLimits = testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/order", params);
        System.out.println(responseOrderLimits.getBody());

//        HttpResponse<?> apiKeyResponse2 = testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/apiKey?reverse=false", params);
//        System.out.println(apiKeyResponse2.getStatus());
//        ObjectMapper m = new ObjectMapper();
//        try {
//            List<ApiKey> responses = m.readValue(apiKeyResponse2.getBody().toString(), new TypeReference<List<ApiKey>>() {
//            });
//            System.out.println(responses);
//        } catch (Exception e) {
//            ExchangeError responses = m.readValue(apiKeyResponse2.getBody().toString(), new TypeReference<ExchangeError>() {
//            });
//            System.out.println(responses.toString());
//        }


//        ApiKeyResponse apiKeyResponse  = (ApiKeyResponse) testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/apiKey?reverse=false", params).getBody();
//        System.out.println(apiKeyResponse);

    }
}
