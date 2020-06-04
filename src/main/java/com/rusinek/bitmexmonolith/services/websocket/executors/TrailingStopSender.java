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
import com.rusinek.bitmexmonolith.util.ResponseModifier;
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
    private final ResponseModifier responseModifier;
    private final ParameterService parameterService;
    private final BitMEXExceptionService bitmexExceptionService;
    private final ObjectMapper objectMapper;
    private final ExchangeService exchangeService;

    public void iterateTrailingStop(Trade trade) {
        trailingStopRepository.findAll().forEach(trailingStop -> {
            if (Double.valueOf(trailingStop.getTrialValue()) > 0) {
                if (trade.getPrice().doubleValue() <= trailingStop.getStartingPrice()) {
                    deleteTrailingStop(trailingStop);
                }
            } else if (Double.valueOf(trailingStop.getTrialValue()) < 0) {
                if (trade.getPrice().doubleValue() >= trailingStop.getStartingPrice()) {
                    deleteTrailingStop(trailingStop);
                }
            }
        });
    }

    private Order sendTrailingStop(TrailingStop trailingStop) {

        log.debug("Attempting to sent trailing stop for user: " + trailingStop.getStopOwner() +
                " for account with id: " + trailingStop.getAccount().getId());

        HttpResponse<String> response = exchangeService.requestApi(ExchangeService.HttpMethod.POST, "/order",
                parameterService.fillParamsForPostRequest(ParameterService.RequestContent.POST_TRAILING_STOP,
                        trailingStop, null, responseModifier.extractInstructions(trailingStop)),
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


    private boolean isTrailingStopSent(Order order) {
        return order.getOrderID() != null;
    }

    private void deleteTrailingStop(TrailingStop trailingStop) {
        if (isTrailingStopSent(Objects.requireNonNull(sendTrailingStop(trailingStop)))) {
            trailingStopRepository.delete(trailingStop);
        }
    }
}
