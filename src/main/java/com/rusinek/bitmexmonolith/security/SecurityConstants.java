package com.rusinek.bitmexmonolith.security;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
public class SecurityConstants {

    static final String SIGN_UP_URLS = "/api/users/**";
    static final String H2_URL = "/h2-console/**";
    static final String SECRET = "SecretKeyToGenJWTsToSecureThisAppIdkWhatMoreToWriteHere";
    public static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final long EXPIRATION_TIME = 1500000;
}
