package com.rusinek.bitmexmonolith.services.websocket.executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.exceptions.BitMEXExceptionService;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.model.response.Order;
import com.rusinek.bitmexmonolith.repositories.TrailingStopRepository;
import com.rusinek.bitmexmonolith.services.exchange.ExchangeService;
import com.rusinek.bitmexmonolith.util.ParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.dto.marketdata.Trade;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by Adrian Rusinek on 22.03.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TrailingStopSender {

    private final TrailingStopRepository trailingStopRepository;
    private final ParameterService parameterService;
    private final BitMEXExceptionService bitmexExceptionService;
    private final ObjectMapper objectMapper;
    private final ExchangeService exchangeService;

    public void iterateAndSentTrailing(Trade trade) {
        trailingStopRepository.findAll().forEach(trailingStop -> {
            if (Double.valueOf(trailingStop.getTrialValue()) > 0) {
                if (trade.getPrice().doubleValue() <= trailingStop.getStartingPrice()) {
                    deleteTrailing(trailingStop);
                }
            } else if (Double.valueOf(trailingStop.getTrialValue()) < 0) {
                if (trade.getPrice().doubleValue() >= trailingStop.getStartingPrice()) {
                        deleteTrailing(trailingStop);
                }
            }
        });
    }

    private Order sentTrailingStop(TrailingStop trailingStop) {

        log.debug("Attempting to sent trailing stop for user: " + trailingStop.getStopOwner() +
                " for account with id: " + trailingStop.getAccount().getId());

        HttpResponse<String> response = exchangeService.requestApi(ExchangeService.HttpMethod.POST, "/order",
                parameterService.fillParamsForPostRequest(ParameterService.RequestContent.POST_TRAILING_STOP,
                        trailingStop, extractInstructions(trailingStop)),
                trailingStop.getAccount().getId(), trailingStop.getStopOwner());
        try {
            return objectMapper.readValue(response.getBody(), new TypeReference<Order>() {
            });
        } catch (JsonProcessingException e) {
            bitmexExceptionService.processErrorResponse(objectMapper, response, trailingStop.getStopOwner(),
                    String.valueOf(trailingStop.getAccount().getId()));
        }
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

    private boolean isTrailingStopSent(Order order) {
        return order.getOrderID() != null;
    }

    private void deleteTrailing(TrailingStop trailingStop) {
        if (isTrailingStopSent(Objects.requireNonNull(sentTrailingStop(trailingStop)))) {
            trailingStopRepository.delete(trailingStop);
        }
    }
}
