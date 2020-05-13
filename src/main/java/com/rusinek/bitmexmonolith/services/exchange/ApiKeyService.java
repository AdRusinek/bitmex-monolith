package com.rusinek.bitmexmonolith.services.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rusinek.bitmexmonolith.exceptions.BitmexExceptionService;
import com.rusinek.bitmexmonolith.model.response.ApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final BitmexExceptionService bitmexExceptionService;
    private final RequestService requestService;


    int requestApiForApiKey(HttpResponse<String> response, ObjectMapper objectMapper, String username, String apiKey) throws UnirestException {

        // casting to model object and excluding permissions list to check if it has order capability
        // if it is not there returns 1 which causing specific validation response
        List<ApiKey> apiKeys;
        try {
            // trying to get all api keys
            apiKeys = objectMapper.readValue(response.getBody(), new TypeReference<List<ApiKey>>() {
            });
        } catch (JsonProcessingException e) {
            bitmexExceptionService.processErrorResponse(objectMapper, response, username, null);
            return 2;
        }
        for (ApiKey key : apiKeys) {
            for (String permission : key.getPermissions()) {
                if (apiKey.equals(key.getId()) && permission.equals("withdraw")) {
                    return 3;
                }
                if (apiKey.equals(key.getId()) && !permission.equals("order")) {
                    return 1;
                }
                // if all went perfect it refreshes user limits to zero
                if (apiKey.equals(key.getId()) && permission.equals("order")) {
                    requestService.refreshUserLimits(username);
                }
            }
        }
        return response.getStatus();
    }
}
