package com.rusinek.bitmexmonolith.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * Created by Adrian Rusinek on 11.05.2020
 **/
@Getter
@Setter
@ToString
public class Position {

    private Long account;
    private String symbol;
    private String currency;
    private String underlying;
    private String quoteCurrency;
    private Float commission;
    private Float initMarginReq;
    private Float maintMarginReq;
    private Long riskLimit;
    private Float leverage;
    private Boolean crossMargin;
    private Float deleveragePercentile;
    private Long rebalancedPnl;
    private Long prevRealisedPnl;
    private Long prevUnrealisedPnl;
    private Float prevClosePrice;
    private Timestamp openingTimestamp;
    private Long openingQty;
    private Long openingCost;
    private Long openingComm;
    private Long openOrderBuyQty;
    private Long openOrderBuyCost;
    private Long openOrderBuyPremium;
    private Long openOrderSellQty;
    private Long openOrderSellCost;
    private Long openOrderSellPremium;
    private Long execBuyQty;
    private Long execBuyCost;
    private Long execSellQty;
    private Long execSellCost;
    private Long execQty;
    private Long execCost;
    private Long execComm;
    private Timestamp currentTimestamp;
    private Long currentQty;
    private Long currentCost;
    private Long currentComm;
    private Long realisedCost;
    private Long unrealisedCost;
    private Long grossOpenCost;
    private Long grossOpenPremium;
    private Long grossExecCost;
    private Boolean isOpen;
    private Float markPrice;
    private Long markValue;
    private Long riskValue;
    private Float homeNotional;
    private Float foreignNotional;
    private String posState;
    private Long posCost;
    private Long posCost2;
    private Long posCross;
    private Long posInit;
    private Long posComm;
    private Long posLoss;
    private Long posMargin;
    private Long posMaint;
    private Long posAllowance;
    private Long taxableMargin;
    private Long initMargin;
    private Long maintMargin;
    private Long sessionMargin;
    private Long targetExcessMargin;
    private Long varMargin;
    private Long realisedGrossPnl;
    private Long realisedTax;
    private Long realisedPnl;
    private Long unrealisedGrossPnl;
    private Long LongBankrupt;
    private Long shortBankrupt;
    private Long taxBase;
    private Float indicativeTaxRate;
    private Long indicativeTax;
    private Long unrealisedTax;
    private Long unrealisedPnl;
    private Float unrealisedPnlPcnt;
    private Float unrealisedRoePcnt;
    private Float simpleQty;
    private Float simpleCost;
    private Float simpleValue;
    private Float simplePnl;
    private Float simplePnlPcnt;
    private Float avgCostPrice;
    private Float avgEntryPrice;
    private Float breakEvenPrice;
    private Float marginCallPrice;
    private Float liquidationPrice;
    private Float bankruptPrice;
    private Timestamp timestamp;
    private Float lastPrice;
    private Long lastValue;
}
