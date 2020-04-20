package com.rusinek.bitmexmonolith.services.websocket.executors;

import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.services.ExchangeService;
import com.rusinek.bitmexmonolith.services.TrailingStopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.dto.marketdata.Trade;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.rusinek.bitmexmonolith.services.ExchangeService.HTTP_METHOD.POST;

/**
 * Created by Adrian Rusinek on 22.03.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TrailingStopSender {

    private final TrailingStopService trailingStopService;
    private final ExchangeService exchangeService;

    public void iterateAndSentTrailing(Trade trade) {
        trailingStopService.findAll().forEach(trailingStop -> {
            if (Double.valueOf(trailingStop.getTrialValue()) > 0) {
                if (trade.getPrice().doubleValue() <= trailingStop.getStartingPrice()) {
                    Map response = sentTrailingStop(trailingStop);
                    if (isTrailingStopIsSent(response)) {
                        trailingStopService.deleteTrailingStop(trailingStop);
                    }
                }
            } else if (Double.valueOf(trailingStop.getTrialValue()) < 0) {
                if (trade.getPrice().doubleValue() >= trailingStop.getStartingPrice()) {
                    Map response = sentTrailingStop(trailingStop);
                    if (isTrailingStopIsSent(response)) {
                        trailingStopService.deleteTrailingStop(trailingStop);
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private Map sentTrailingStop(TrailingStop trailingStop) {
        log.debug("Attempting to sent trailing stop for user: " + trailingStop.getTrailingStopOwner());

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", "XBTUSD");
        params.put("ordType", "Stop");
        params.put("pegPriceType ", "TrailingStopPeg");
        params.put("pegOffsetValue", trailingStop.getTrialValue());
        params.put("orderQty", trailingStop.getQuantity());

        try {
            return (Map<String, Object>) exchangeService
                    .requestApi(POST, "/order", params,
                            trailingStop.getAccount().getId(), trailingStop.getTrailingStopOwner());
        } catch (ClassCastException ex) {
            log.debug("Could not sent trailing stop to Exchange.");
            ex.printStackTrace();
        }
        return null;
    }

    private boolean isTrailingStopIsSent(Map response) {
        try {
            String orderId = response.get("orderID").toString();
            return !orderId.isEmpty();
        } catch (NullPointerException ex) {
            log.debug("Null pointer due to not existing response");
        }
        return false;
    }
}
