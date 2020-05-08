package com.rusinek.bitmexmonolith.util;

import java.time.Instant;

/**
 * Created by Adrian Rusinek on 07.05.2020
 **/
public class TestingPurposes {

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis() / 1000L );
        System.out.println(Instant.now().getEpochSecond());
        System.out.println(System.currentTimeMillis() / 1000L + 60);
    }
}
