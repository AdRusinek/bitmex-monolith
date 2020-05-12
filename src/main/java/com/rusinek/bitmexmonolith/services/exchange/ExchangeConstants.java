package com.rusinek.bitmexmonolith.services.exchange;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
public class ExchangeConstants {

    // 20 opened orders [MAX]
    public static final String OPENED_LIMIT_ORDERS = "/order?symbol=XBT&filter=%7B%22symbol%22%3A%20%22XBTUSD%22%2C%20%22ordType%22%3A%20%22Limit%22%2C%20%22open%22%3A%20true%7D&count=20&reverse=false";
    public static final String OPENED_STOP_ORDERS = "/order?symbol=XBT&filter=%7B%22symbol%22%3A%20%22XBTUSD%22%2C%20%22ordType%22%3A%20%5B%22Stop%22%2C%20%22MarketIfTouched%22%2C%20%22StopLimit%22%2C%20%22LimitIfTouched%22%5D%2C%20%22open%22%3A%20true%7D&count=6&reverse=false";
    public static final String OPEN_POSITION = "/position?filter=%7B%22symbol%22%3A%20%22XBTUSD%22%7D";
    public static final String GET_APIKEY_URL = "/apiKey?reverse=false";

}
