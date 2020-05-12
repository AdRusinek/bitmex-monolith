package com.rusinek.bitmexmonolith.services.websocket.executors;

import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.repositories.TrailingStopRepository;
import com.rusinek.bitmexmonolith.services.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.dto.marketdata.Trade;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrian Rusinek on 22.03.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TrailingStopSender {

    private final TrailingStopRepository trailingStopRepository;
    private final ExchangeService exchangeService;

    public void iterateAndSentTrailing(Trade trade) {
        trailingStopRepository.findAll().forEach(trailingStop -> {
            if (Double.valueOf(trailingStop.getTrialValue()) > 0) {
                if (trade.getPrice().doubleValue() <= trailingStop.getStartingPrice()) {
                    Map response = sentTrailingStop(trailingStop);
                    if (isTrailingStopSent(response)) {
                        trailingStopRepository.delete(trailingStop);
                    }
                }
            } else if (Double.valueOf(trailingStop.getTrialValue()) < 0) {
                if (trade.getPrice().doubleValue() >= trailingStop.getStartingPrice()) {
                    Map response = sentTrailingStop(trailingStop);
                    if (isTrailingStopSent(response)) {
                        trailingStopRepository.delete(trailingStop);
                    }
                }
            }
        });
    }

    private Map sentTrailingStop(TrailingStop trailingStop) {

        log.debug("Attempting to sent trailing stop for user: " + trailingStop.getTrailingStopOwner());

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", "XBTUSD");
        params.put("ordType", "Stop");
        params.put("pegPriceType ", "TrailingStopPeg");
        params.put("pegOffsetValue", trailingStop.getTrialValue());
        params.put("orderQty", trailingStop.getQuantity());
        params.put("execInst", extractInstructions(trailingStop));

//        try {
//            return (Map<String, Object>) exchangeService
//                    .requestApiWithPost("/order", params,
//                            trailingStop.getAccount().getId(), trailingStop.getTrailingStopOwner());
//        } catch (ClassCastException ex) {
//            log.debug("Could not sent trailing stop to Exchange.");
//            ex.printStackTrace();
//        }
        return null;
    }

    private String extractInstructions(TrailingStop trailingStop) {
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

    private boolean isTrailingStopSent(Map response) {
        try {
            String orderId = response.get("orderID").toString();
            return !orderId.isEmpty();
        } catch (Exception ex) {
            log.error("Null pointer due to not existing response");
        }
        return false;
    }
}
