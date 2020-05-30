package com.rusinek.bitmexmonolith.services.exchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.exceptions.exchangeExceptions.ExchangeLimitsException;
import com.rusinek.bitmexmonolith.model.response.Order;
import com.rusinek.bitmexmonolith.util.ParameterService;
import com.rusinek.bitmexmonolith.util.ResponseModifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


/**
 * Created by Adrian Rusinek on 21.02.2020
 **/
@Service
@RequiredArgsConstructor
public class LimitOrderService {

    private final ExchangeService exchangeService;
    private final ParameterService parameterService;
    private final ObjectMapper objectMapper;
    private final ResponseModifier responseModifier;

    public List<Order> requestLimitOrders(Principal principal, String accountId) {

        HttpResponse<String> response = exchangeService.requestApi(ExchangeService.HttpMethod.GET,
                "/order", parameterService
                        .fillParamsForGetRequest(ParameterService.RequestContent.GET_LIMIT_ORDERS),
                Long.valueOf(accountId), principal.getName());

        try {
            return responseModifier
                    .extractAndSetNewQuantity(objectMapper.readValue(response.getBody(), new TypeReference<List<Order>>() {
                    }));
        } catch (Exception e) {
            throw new ExchangeLimitsException("BitMEX is currently not ready to use or you exceeded api limits.");
        }
    }
}
