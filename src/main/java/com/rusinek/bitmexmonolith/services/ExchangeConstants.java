package com.rusinek.bitmexmonolith.services;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
public class ExchangeConstants {

    // 20 opened orders [MAX]
    public static final String OPENED_LIMIT_ORDERS = "/order?symbol=XBT&filter=%7B%22open%22%3A%20%22true%22%2C%20%22ordType%22%3A%20%22Limit%22%7D&count=20&reverse=false";
    public static final String OPENED_STOP_ORDERS = "/order?filter=%7B%22open%22%3A%22true%22%2C%20%22ordType%22%3A%22Stop%22%7D&count=50&reverse=false";
    public static final String OPEN_POSITION = "/position?filter=%7B%22symbol%22%3A%20%22XBTUSD%22%2C%22isOpen%22%3A%20%22True%22%7D";
    public static final String GET_APIKEY_URL = "/apiKey?reverse=false";

}
