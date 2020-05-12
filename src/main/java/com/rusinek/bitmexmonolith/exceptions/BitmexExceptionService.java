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

    public void processErrorResponse(ObjectMapper objectMapper, HttpResponse<String> response, String username, String accountId) {
        //if error occurred BitMEX sends message in this format
        ExchangeError exchangeError = null;
        try {
            exchangeError = objectMapper.readValue(response.getBody(), new TypeReference<ExchangeError>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize properties.");
            e.getMessage();
        }
        // if BitMEX returned error get properties from it and print log
        if (exchangeError != null) {
         String errorResponse;

         errorResponse = "BitMEX returned error with message '" + exchangeError.getError().getMessage() + "', Type of error: '" +
                 exchangeError.getError().getName() + "'. Status: " + response.getStatus() + ". Error happened to user '" + username + "'";
         if (accountId != null) {
            errorResponse += " on account with id '" + accountId + "'. ";
         } else {
             errorResponse += ".";
         }
            log.error(errorResponse);
        }
    }
}
