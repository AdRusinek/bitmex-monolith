package com.rusinek.bitmexmonolith.services;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
public class ExchangeConstants {

    public static final Integer TIME_TO_RESET_REQUEST_LIMIT = 31 * 1000;
    public static final Integer REQUEST_RATE_LIMIT = 30;

    public static final String OPENED_LIMIT_ORDERS = "/order?filter=%7B%22open%22%3A%22true%22%2C%20%22ordType%22%3A%22Limit%22%7D&count=50&reverse=false";
    public static final String OPENED_STOP_ORDERS = "/order?filter=%7B%22open%22%3A%22true%22%2C%20%22ordType%22%3A%22Stop%22%7D&count=50&reverse=false";
    public static final String POSITION = "/position?filter=%7B%22symbol%22%3A%20%22XBTUSD%22%7D&columns=%5B%22currentQty%22%2C%22avgEntryPrice%22%2C%22maintMargin%22%5D";

}
