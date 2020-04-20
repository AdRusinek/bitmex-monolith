package com.rusinek.bitmexmonolith.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rusinek.bitmexmonolith.services.ExchangeConstants.OPENED_LIMIT_ORDERS;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
public class BitMexUtil {
    private static final String basePath = "/api/v1";
    private static final int expireSeconds = 20;
    private static final Charset charset = Charset.forName("UTF-8");
    private static final Gson gson = new Gson();

    private static BitMexUtil instance = null;

    public static BitMexUtil getInstance() {
        if (instance == null) {
            instance = testInstance();

            // This can be removed if you don't have the ssl connection problem
//            Unirest.setHttpClient(getUnsafeHttpClient());
        }
        return instance;
    }

    private static BitMexUtil realInstance() {
        return new BitMexUtil("https://www.bitmex.com",
                "your real public key (short one)",
                "your real secret key (long one)");
    }

    //todo zmien zanim pushniesz
    private static BitMexUtil testInstance() {
        return new BitMexUtil("https://testnet.bitmex.com",
                "G1AsSffAD543JsRF6XyOuwXL",
                "etgpyewzmxriXs1rHUXOLOTq99lFicRNeo6h8EAGfYziH_3W");
    }

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

    public Object requestApi(HTTP_METHOD method, String varPath) {
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
            return gson.fromJson(response.getBody(), Object.class);

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

        BitMexUtil bitMexUtil = BitMexUtil.getInstance();

        Map<String, Object> params = new HashMap<>();
//        params.put("currency", "XBt");
//        Map<String, Object> wallet = (Map<String, Object>) bitMexUtil.requestApi(HTTP_METHOD.GET, "/user/wallet", params);
//        System.out.println(wallet);
//
        System.out.println("====================================================");
        // get positions
        ArrayList positionList = (ArrayList) bitMexUtil.requestApi(HTTP_METHOD.GET, "/position?filter=%7B%22symbol%22%3A%20%22XBTUSD%22%7D&columns=%5B%22currentQty%22%2C%22avgEntryPrice%22%2C%22maintMargin%22%5D");
        System.out.println(positionList);

        System.out.println("====================================================");
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
