package com.rusinek.bitmexmonolith.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.rusinek.bitmexmonolith.model.response.ExchangeError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Adrian Rusinek on 11.05.2020
 **/
@Slf4j
@Service
public class BitmexExceptionService {

    public void processErrorResponse(ObjectMapper objectMapper, HttpResponse<String> response) {
        //if error occurred BitMEX sends message in this format
        ExchangeError exchangeError = null;
        try {
            exchangeError = objectMapper.readValue(response.getBody(), new TypeReference<ExchangeError>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize properties.");
            e.getMessage();
        }
        if (exchangeError != null) {
            log.error("BitMEX returned error with message '" + exchangeError.getError().getMessage() + "', Type of error: '" +
                    exchangeError.getError().getName() + "'. Status: " + response.getStatus() + ". ");
        }
    }
}
