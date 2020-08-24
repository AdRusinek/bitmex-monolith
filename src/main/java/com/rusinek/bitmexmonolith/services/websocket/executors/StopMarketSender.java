package com.rusinek.bitmexmonolith.services.websocket.executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.exceptions.BitMEXExceptionService;
import com.rusinek.bitmexmonolith.model.StopMarket;
import com.rusinek.bitmexmonolith.model.response.Order;
import com.rusinek.bitmexmonolith.repositories.StopMarketRepository;
import com.rusinek.bitmexmonolith.services.exchange.ExchangeService;
import com.rusinek.bitmexmonolith.util.ParameterService;
import com.rusinek.bitmexmonolith.util.ResponseModifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.dto.marketdata.Trade;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by Adrian Rusinek on 04.06.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class StopMarketSender {

    private final StopMarketRepository stopMarketRepository;
    private final ResponseModifier responseModifier;
    private final ParameterService parameterService;
    private final BitMEXExceptionService bitmexExceptionService;
    private final ObjectMapper objectMapper;
    private final ExchangeService exchangeService;

    public void iterateStopMarket(Trade trade) {
        stopMarketRepository.findAll().forEach(stopMarket -> {
            if (stopMarket.getStopType().equals("Sell")) {
                if (stopMarket.getStartingPrice() < trade.getPrice().doubleValue()) {
                    deleteStopMarket(stopMarket);
                }
            } else if (stopMarket.getStopType().equals("Buy")) {
                if (stopMarket.getStartingPrice() >= trade.getPrice().doubleValue()) {
                    deleteStopMarket(stopMarket);
                }
            }
        });
    }

    private Order sendStopMarket(StopMarket stopMarket) {

        log.debug("Attempting to sent Stop Market order for user: " + stopMarket.getStopOwner() +
                " for account with id: " + stopMarket.getAccount().getId());

        HttpResponse<String> response = exchangeService.requestApi(ExchangeService.HttpMethod.POST, "/order",
                parameterService.fillParamsForPostRequest(ParameterService.RequestContent.POST_STOP_MARKET,
                        null, stopMarket, responseModifier.extractInstructions(stopMarket)),
                stopMarket.getAccount().getId(), stopMarket.getStopOwner());
        try {
            return objectMapper.readValue(response.getBody(), new TypeReference<Order>() {
            });
        } catch (JsonProcessingException e) {
            bitmexExceptionService.processErrorResponse(objectMapper, response, stopMarket.getStopOwner(),
                    String.valueOf(stopMarket.getAccount().getId()));
        }
        return null;
    }

    private boolean isStopMarketSent(Order order) {
        return order.getOrderID() != null;
    }

    private void deleteStopMarket(StopMarket stopMarket) {
        if (isStopMarketSent(Objects.requireNonNull(sendStopMarket(stopMarket)))) {
            stopMarketRepository.delete(stopMarket);
        }
    }
}
