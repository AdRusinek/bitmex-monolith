package com.rusinek.bitmexmonolith.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by Adrian Rusinek on 11.05.2020
 **/
@Getter
@Setter
@ToString
public class Order {

    private UUID orderID;
    private String clOrdID;
    private String clOrdLinkID;
    private Long account;
    private String symbol;
    private String side;
    private Float simpleOrderQty;
    private Long orderQty;
    private Float price;
    private Long displayQty;
    private Float stopPx;
    private Float pegOffsetValue;
    private String pegPriceType;
    private String currency;
    private String settlCurrency;
    private String ordType;
    private String timeInForce;
    private String execInst;
    private String contingencyType;
    private String exDestination;
    private String ordStatus;
    private String triggered;
    private Boolean workingIndicator;
    private String ordRejReason;
    private Float simpleLeavesQty;
    private Long leavesQty;
    private Float simpleCumQty;
    private Long cumQty;
    private Float avgPx;
    private String multiLegReportingType;
    private String text;
    private Timestamp transactTime;
    private Timestamp timestamp;
}
