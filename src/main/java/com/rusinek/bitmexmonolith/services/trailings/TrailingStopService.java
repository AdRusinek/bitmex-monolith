package com.rusinek.bitmexmonolith.services.trailings;

import com.rusinek.bitmexmonolith.model.TrailingStop;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;

/**
 * Created by Adrian Rusinek on 25.02.2020
 **/
public interface TrailingStopService {

    ResponseEntity<?> saveTrailingStop(TrailingStop trailingStop, BindingResult result, Principal principal, String accountId);

    List<TrailingStop> getAllTrailingStops(String username);

    void deleteTrailingStop(TrailingStop trailingStop);

    ResponseEntity<List<TrailingStop>> getAllTrailingStopsByCredentialsId(Principal principal, String accountId);

    Iterable<TrailingStop> findAll();
}
