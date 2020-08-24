package com.rusinek.bitmexmonolith.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.model.response.Order;
import lombok.extern.slf4j.Slf4j;
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

import static com.rusinek.bitmexmonolith.services.exchange.ExchangeService.HttpMethod.GET;

/**
 * Created by Adrian Rusinek on 07.05.2020
 **/
@Slf4j
public class TestingPurposes {

    private static final String basePath = "/api/v1";
    private static final int expireSeconds = 5;
    private static final Charset charset = Charset.forName("UTF-8");
    private static final Gson gson = new Gson();
    private static TestingPurposes instance = null;
    private static TestingPurposes instance2 = null;
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
            instance = testInstance3();

            // This can be removed if you don't have the ssl connection problem
//            Unirest.setHttpClient(getUnsafeHttpClient());
        }
        return instance;
    }

    public static TestingPurposes getInstance2() {
        if (instance2 == null) {
            instance2 = testInstance();

            // This can be removed if you don't have the ssl connection problem
//            Unirest.setHttpClient(getUnsafeHttpClient());
        }
        return instance2;
    }

    private static TestingPurposes testInstance() {
        return new TestingPurposes("https://testnet.bitmex.com",
                "PRNL8mhAghyJIisZ3VexVi7z",
                "qNnYse8Qzrk1knrHj9u2FtkkD-Dmo95fqbwek1TqBO6CYcmq");
    }

    private static TestingPurposes testInstance2() {
        return new TestingPurposes("https://testnet.bitmex.com",
                "LgzGvg0WOVCUlEtcE5WUMAJv",
                "-yrietGclsp5MPDqyYhDYYR5sTGE81PNNdcmkMEYwHEQyL_G");
    }

    private static TestingPurposes testInstance4() {
        return new TestingPurposes("https://testnet.bitmex.com",
                "G1AsSffAD543JsRF6XyOuwXL",
                "etgpyewzmxriXs1rHUXOLOTq99lFicRNeo6h8EAGfYziH_3W");
    }


    private static TestingPurposes testInstance3() {
        return new TestingPurposes("https://testnet.bitmex.com",
                "MjLi9-loU_rEU9EYk6qv_T-K",
                "CWJw70BmCZlnWSHXzAre1C-CcY743faKRTAd4ZCHaBNxJNDT");
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

    static String extractInstructions(TrailingStop trailingStop) {
        String finalInstructions = "";

        //if you only set Mark without close on trigger the exactInst on BitMEX is empty
        if (trailingStop.getExecInst().equals("MarkPrice") && !trailingStop.getCloseOnTrigger()) {
            finalInstructions = "";
        }
        //if you set Mark with close on trigger with close on trigger then on BitMEX exactInst is "Close"
        if (trailingStop.getExecInst().equals("MarkPrice") && trailingStop.getCloseOnTrigger()) {
            finalInstructions = "Close";
        }
        if (trailingStop.getCloseOnTrigger()) {
            finalInstructions = "Close, " + trailingStop.getExecInst();
        }
        if (!trailingStop.getCloseOnTrigger()) {
            finalInstructions = trailingStop.getExecInst();
        }
        return finalInstructions;
    }

    //todo trzeba dodac i usuwacc
    public static void main(String[] args) throws JsonProcessingException {


//        String apiKey = "G1AsSffAD543JsRF6XyOuwXL";
//        String apiKeySecret = "etgpyewzmxriXs1rHUXOLOTq99lFicRNeo6h8EAGfYziH_3W";
//
//
//        System.out.println(apiKeySecret.length());

//        System.out.println(apiKey.length());
//        System.out.println(encodeApiKey(apiKey).length());
//        System.out.println(encodeApiKey(apiKey2).length());

        //        System.out.println(apiKey.length());
//        String encodeApiKey = encodeApiKey(apiKey);
//        System.out.println(encodeApiKey);
//        String decryptApiKey = decodeApiKey(encodeApiKey);
//        System.out.println(decryptApiKey);

//        String secretKey = "etgpyewzmxriXs1rHUXOLOTq99lFicRNeo6h8EAGfYziH_3W";
//        String encodeSecretKey = encodeSecretKey(secretKey);
//        System.out.println(encodeSecretKey);
//        String decodeSecretKey = decodeSecretKey(secretKey);
//        System.out.println(decodeSecretKey);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        for (int i = 0; i < 5; i++) {
//            TestingPurposes testingPurposes = TestingPurposes.getInstance();
//            Map<String, Object> params = new HashMap<>();
//            Map<String, Object> filterMap = new HashMap<>();
//            filterMap.put("isOpen", true);
//            filterMap.put("symbol", "XBTUSD");
//            params.put("filter", gson.toJson(filterMap));
//            HttpResponse<?> responsePosition = testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/position", params);
//            System.out.println(responsePosition.getBody());
//            System.out.println(responsePosition.getHeaders().get("X-RateLimit-Remaining"));
//        }

        TrailingStop trailingStop = new TrailingStop();
        trailingStop.setQuantity(Double.valueOf(30.0));
        trailingStop.setExecInst("MarkPrice");
        trailingStop.setCloseOnTrigger(false);

        TestingPurposes testingPurposes2 = TestingPurposes.getInstance2();
        Map<String, Object> params = new HashMap<>();

        params.put("symbol", "XBTUSD");
        params.put("ordType", "Stop");
        params.put("side", "sell");
        params.put("orderQty", 12);
        params.put("stopPx", 9450);
        params.put("execInst", extractInstructions(trailingStop));

        System.out.println("PARAMS: " + params);

        HttpResponse<?> responsePosition2 = testingPurposes2.requestApi(BitMexUtil.HTTP_METHOD.POST, "/order", params);
        System.out.println("Response: " + responsePosition2.getBody());
        System.out.println(responsePosition2.getHeaders().get("X-RateLimit-Remaining"));
        System.out.println(responsePosition2);
////
//        HttpResponse<String> response = testingPurposes.requestApiWithGet(BitMexUtil.HTTP_METHOD.GET, OPENED_STOP_ORDERS);
//
//        List<Order> orders = null;
//        try {
//            orders = objectMapper.readValue(response.getBody(), new TypeReference<List<Order>>() {
//            });
//        } catch (JsonProcessingException e) {
//
//        }

//        System.out.println(orders);


//        orders.forEach(order -> {
//            System.out.println(order.getOrderQty());
//        });
//
//        testingPurposes.extractAndSetNewPrice(orders).forEach(order -> {
//            System.out.println(order.getOrderQty());
//        });
//
//        params.clear();
//        filterMap.clear();

//        filterMap.put("symbol", "XBTUSD");
//        filterMap.put("ordType", "Limit");
//        filterMap.put("open", true);
//
//        params.put("symbol", "XBTUSD");
//        params.put("ordType", "Stop");
//        params.put("pegPriceType ", "TrailingStopPeg");
//        params.put("pegOffsetValue", 20);
//        params.put("orderQty", 20);
//        params.put("execInst", "MarkPrice, Close");
//
//        HttpResponse<?> responseOrderLimits = testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.POST, "/order", params);
//        Order responses = objectMapper.readValue(responseOrderLimits.getBody().toString(), new TypeReference<Order>() {
//        });
//        System.out.println(responseOrderLimits.getBody());
//        String limitHeader = String.valueOf(responseOrderLimits.getHeaders().get("X-RateLimit-Remaining"));
//        System.out.println(limitHeader);
//        System.out.println(responses.getOrderID());
//        HttpResponse<?> apiKeyResponse2 = testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/apiKey?reverse=false", params);
//        System.out.println(apiKeyResponse2.getStatus());
//        ObjectMapper m = new ObjectMapper();
//        try {
//            List<ApiKey> apiKeys = m.readValue(apiKeyResponse2.getBody().toString(), new TypeReference<List<ApiKey>>() {
//            });
//            System.out.println(apiKeys);
//            for (ApiKey key : apiKeys) {
//                for (String permission : key.getPermissions()) {
//                    if ("LgzGvg0WOVCUlEtcE5WUMAJv".equals(key.getId()) && permission.equals("withdraw")) {
//                        System.out.println("w");
//                    }
//                }
//            }
//        } catch (Exception e) {
//            ExchangeError responses = m.readValue(apiKeyResponse2.getBody().toString(), new TypeReference<ExchangeError>() {
//            });
//            System.out.println(responses.toString());
//        }


//            System.out.println(TestingPurposes.class.getResource(".").getFile());
//        FileWriter outputFile = null;
//        try {
//            Resource resource = new ClassPathResource("tempQR.jpg");
//            System.out.println(resource.toString());
//            outputFile.write("dsds");
//            outputFile.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        HttpResponse<String> apiKeyResponse  = (HttpResponse<String>) testingPurposes.requestApi(BitMexUtil.HTTP_METHOD.GET, "/apiKey?reverse=false", params).getBody();
//        System.out.println(apiKeyResponse.getBody());

    }

    private List<Order> extractAndSetNewPrice(List<Order> orders) {
        return orders.stream().peek(order -> {
            if (order.getSide().equals("Sell")) {
                order.setOrderQty(-order.getOrderQty());
            }
        }).collect(Collectors.toList());
    }


//    static char[] getModifiedApiKey(String apiKeyToModified, boolean isEncoded) {
//        char[] charApiKeyArray = apiKeyToModified.toCharArray();
//        int ascii;
//
//        ascii = (int) charApiKeyArray[1];
//        charApiKeyArray[1] = (char) (isEncoded ? ascii + 2 : ascii - 2);
//        ascii = (int) charApiKeyArray[4];
//        charApiKeyArray[4] = (char) (isEncoded ? ascii - 1 : ascii + 1);
//        ascii = (int) charApiKeyArray[13];
//        charApiKeyArray[13] = (char) (isEncoded ? ascii + 1 : ascii - 1);
//        ascii = (int) charApiKeyArray[18];
//        charApiKeyArray[18] = (char) (isEncoded ? ascii - 2 : ascii + 2);
//        ascii = (int) charApiKeyArray[23];
//        charApiKeyArray[23] = (char) (isEncoded ? ascii - 3 : ascii + 3);
//
//        return charApiKeyArray;
//    }
//
//
//    static String encodeApiKey(String apiKeyToEncode) {
//        StringBuilder buildFalseApiKey = new StringBuilder(String.valueOf(getModifiedApiKey(apiKeyToEncode,false)));
//
//        buildFalseApiKey
//                .insert(3, "b")
//                .insert(5, "t")
//                .insert(9, "H")
//                .insert(13, "G")
//                .insert(16, "l")
//                .insert(19, "a")
//                .insert(22, "m")
//                .insert(24, "P");
//
//
//        return String.valueOf(buildFalseApiKey);
//    }
//
//    static String decodeApiKey(String apiKeyToDecode) {
//        StringBuilder falseApiKey = new StringBuilder(apiKeyToDecode);
//
//        falseApiKey
//                .deleteCharAt(24)
//                .deleteCharAt(22)
//                .deleteCharAt(19)
//                .deleteCharAt(16)
//                .deleteCharAt(13)
//                .deleteCharAt(9)
//                .deleteCharAt(5)
//                .deleteCharAt(3);
//
//        return String.valueOf(getModifiedApiKey(String.valueOf(falseApiKey), true));
//    }
//
//    static String encodeApiKeySecret(String apiKeySecretToEncode) {
//        StringBuilder falseApiKeySecret = new StringBuilder(apiKeySecretToEncode);
//
//        falseApiKeySecret
//                .insert(6, "r")
//                .insert(17, "p")
//                .insert(25, "Q")
//                .insert(37, "M");
//
//        return String.valueOf(falseApiKeySecret);
//    }
//
//    static String decodeApiKeySecret(String apiKeySecretToDecode) {
//        StringBuilder falseApiKeySecret = new StringBuilder(apiKeySecretToDecode);
//
//        falseApiKeySecret
//                .deleteCharAt(37)
//                .deleteCharAt(25)
//                .deleteCharAt(17)
//                .deleteCharAt(6);
//
//        return String.valueOf(falseApiKeySecret);
//    }

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
        System.out.println("Sign" + signContent);

        // set apiSignature
        HashFunction hashFunc = Hashing.hmacSha256(apiKeySecret.getBytes(charset));
        HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(charset));
        String apiSignature = hashCode.toString();
        System.out.println(apiSignature);
        System.out.println("*********************************************");
        System.out.println(apiSignature);
        System.out.println("*********************************************");
        // get headers
        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("api-expires", String.valueOf(apiExpires));
        headers.put("api-signature", apiSignature);

        System.out.println(apiSignature);
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
        String url = "https://bitmex.com/" + path;

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
}
