package com.rusinek.bitmexmonolith.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
@Slf4j
public class BitMexUtil {
    private static final String basePath = "/api/v1";
    private static final int expireSeconds = 20;
    private static final Charset charset = Charset.forName("UTF-8");
    private static final Gson gson = new Gson();

    private static BitMexUtil instance1 = null;
    private static BitMexUtil instance2 = null;

    public static BitMexUtil getInstance1() {
        if (instance1 == null) {
            instance1 = testInstance1();

            // This can be removed if you don't have the ssl connection problem
//            Unirest.setHttpClient(getUnsafeHttpClient());
        }
        return instance1;
    }

    public static BitMexUtil getInstance2() {
        if (instance2 == null) {
            instance2 = testInstance2();

            // This can be removed if you don't have the ssl connection problem
//            Unirest.setHttpClient(getUnsafeHttpClient());
        }
        return instance2;
    }

    private static BitMexUtil realInstance() {
        return new BitMexUtil("https://www.bitmex.com",
                "your real public key (short one)",
                "your real secret key (long one)");
    }

    //todo zmien zanim pushniesz


    private String host = null;
    private String apiKey = null;
    private String apiKeySecret = null;

    private BitMexUtil(String host, String apiKey, String apiKeySecret) {
        this.host = host;
        this.apiKey = apiKey;
        this.apiKeySecret = apiKeySecret;
    }

    public Object requestApi(HTTP_METHOD method, String varPath, Map<String, Object> params) {
// set api-expires
        String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + expireSeconds);

        // get signContent
        String paramsEncodedStr = getEncodedStrOfParams(params);
        String path = basePath + varPath;
        if ((method == HTTP_METHOD.GET) && !paramsEncodedStr.isEmpty()) {
            path += "?" + paramsEncodedStr;
        }
        String url = host + path;
        String signContent = method.toString() + path + apiExpires;
        if (method == HTTP_METHOD.POST) {
            signContent += paramsEncodedStr;
        }

        // set apiSignature
        HashFunction hashFunc = Hashing.hmacSha256(apiKeySecret.getBytes(charset));
        HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(charset));
        String apiSignature = hashCode.toString();

        // get headers
        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("api-expires", apiExpires);
        headers.put("api-signature", apiSignature);

        try {
            HttpResponse<String> response = null;
            if (method == HTTP_METHOD.GET) {
                response = Unirest.get(url)
                        .headers(headers)
                        .asString();

            } else if (method == HTTP_METHOD.POST) {
                response = Unirest.post(url)
                        .headers(headers)
                        .fields(params)
                        .asString();
            }
            return gson.fromJson(response.getBody(), Object.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Integer requestApi(HTTP_METHOD method, String varPath) {
        // set api-expires
        String apiExpires = String.valueOf(System.currentTimeMillis() / 1000 + expireSeconds);

        // get signContent
        String path = basePath + varPath;
        String url = host + path;

        String signContent = method.toString() + path + apiExpires;

        // set apiSignature
        HashFunction hashFunc = Hashing.hmacSha256(apiKeySecret.getBytes(charset));
        HashCode hashCode = hashFunc.hashBytes(signContent.getBytes(charset));
        String apiSignature = hashCode.toString();

        // get headers
        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("api-expires", apiExpires);
        headers.put("api-signature", apiSignature);

        try {
            HttpResponse<String> response = null;
            if (method == HTTP_METHOD.GET) {
                response = Unirest.get(url)
                        .headers(headers)
                        .asString();

            } else if (method == HTTP_METHOD.POST) {
                response = Unirest.post(url)
                        .headers(headers)
                        .asString();
            }
            //todo zrob z tymi limitami porzadnie
//            System.out.println("***************************************************************************");
//            System.out.println(response.getHeaders().get("X-RateLimit-Remaining"));
//
//            String limitHeader = String.valueOf(response.getHeaders().get("X-RateLimit-Remaining"));
//            Integer limit = Integer.valueOf(limitHeader.substring(1, limitHeader.length() - 1));
//            System.out.println(limit);
//
//
//            String seconds = String.valueOf(response.getHeaders().get("X-RateLimit-Reset"));
//            Integer actualSeconds = Integer.valueOf(seconds.substring(1, seconds.length() - 1));
//            System.out.println(actualSeconds);
//            Date currentTime = new Date(actualSeconds*1000);
//            System.out.println(currentTime);
//
//            System.out.println(response.getHeaders().get("X-RateLimit-Reset"));
//            System.out.println("***************************************************************************");
            if (response.getBody().contains("error")) {
                log.error("Error occurred while requesting BitMEX API. Status code: '"
                        + response.getStatus() + "' with description: " + response.getBody());
//                return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getStatus()));
            }
            System.out.println("Headers" + response.getHeaders());
            System.out.println("Status" + response.getStatus());
            return response.getStatus();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static HttpClient unsafeHttpClient = null;

    private static HttpClient getUnsafeHttpClient() {
        if (unsafeHttpClient == null) {
            try {
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy() {
                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                }).build();

                unsafeHttpClient = HttpClients.custom().setSSLContext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();

            } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                e.printStackTrace();
            }

        }
        return unsafeHttpClient;
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

    ;

    public static void main(String[] args) {
// get money

        BitMexUtil bitMexUtil = BitMexUtil.getInstance1();

//        Map<String, Object> params = new HashMap<>();
//        params.put("currency", "XBt");
//        Map<String, Object> wallet = (Map<String, Object>) bitMexUtil.requestApi(HTTP_METHOD.GET, "/user/wallet", params);
//        System.out.println(wallet);
//

        System.out.println("====================================================");
        // get positions
        int status = bitMexUtil.requestApi(HTTP_METHOD.GET, "/apiKey?reverse=false");
        System.out.println(status == HttpStatus.OK.value());


        System.out.println("===================================================");


        Map<String, Object> params = new HashMap<>();
        params.put("symbol", "XBTUSD");
        params.put("ordType", "Stop");
        params.put("pegPriceType ", "TrailingStopPeg");
        params.put("pegOffsetValue", "-40");
        params.put("orderQty", "30");
        params.put("execInst", "Close, MarkPrice");

        LinkedTreeMap o = (LinkedTreeMap) bitMexUtil.requestApi(HTTP_METHOD.POST, "/order", params);
        System.out.println("**************");
        System.out.println(o);
        System.out.println("**************");
        System.out.println("===================================================");
//            BitMexUtil bitMexUtil2 = BitMexUtil.getInstance2();
//
////        Map<String, Object> params2 = new HashMap<>();
////        params.put("currency", "XBt");
////        Map<String, Object> wallet = (Map<String, Object>) bitMexUtil.requestApi(HTTP_METHOD.GET, "/user/wallet", params);
////        System.out.println(wallet);
////
//
//            System.out.println("====================================================");
//            // get positions
//            Object positionList2 = bitMexUtil2.requestApi(HTTP_METHOD.GET, "/position?filter=%7B%22symbol%22%3A%20%22XBTUSD%22%7D&columns=%5B%22currentQty%22%2C%22avgEntryPrice%22%2C%22maintMargin%22%5D");
//            System.out.println(positionList2);
//
//            System.out.println("===================================================");
//
//        // get api key
//        params.clear();
//        params.put("reverse",true);
//        List<String> keys = (List<String>) bitMexUtil.requestApi(HTTP_METHOD.GET,"/apiKey",params);
//        System.out.println(keys);
//
//        System.out.println("====================================================");
//
//        // get user info
//        params.clear();
//        Map<String,Object> user = (Map<String,Object>) bitMexUtil.requestApi(HTTP_METHOD.GET,"/user",params);
//        System.out.println(user);
//
//        System.out.println("====================================================");

        // get orders
//        params.clear();
//        params.put("open", "true");
//        params.put("ordType", "Stop");
//        params.put("reverse", "false");
//        params.put("count", "1");

//        System.out.println("======================================================");
//        List<LinkedTreeMap> orders = (List<LinkedTreeMap>) bitMexUtil.requestApi(HTTP_METHOD.GET, OPENED_LIMIT_ORDERS);
//        System.out.println(orders);
//
//        System.out.println("=====================================================");
    }
}
