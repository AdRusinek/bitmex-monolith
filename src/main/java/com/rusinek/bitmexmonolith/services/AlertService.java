package com.rusinek.bitmexmonolith.services;


import com.rusinek.bitmexmonolith.controllers.mappers.AlertMapper;
import com.rusinek.bitmexmonolith.dto.AlertDto;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.exceptions.alertException.AlertAlreadyExistsException;
import com.rusinek.bitmexmonolith.exceptions.alertException.AlertAmountException;
import com.rusinek.bitmexmonolith.exceptions.alertException.AlertIdException;
import com.rusinek.bitmexmonolith.model.Alert;
import com.rusinek.bitmexmonolith.repositories.AlertRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final MapValidationErrorService errorService;
    private final UserRepository userRepository;
    private final AlertMapper alertMapper;

    public ResponseEntity<?> saveAlertToAccount(Alert alert, BindingResult result, Principal principal) {

        // processErrorResponse possible errors that may come from model annotations
        if (result.hasErrors()) {
            return errorService.validateErrors(result);
        }

        List<AlertDto> allAlertsByAlertOwner = getAllAlertsByAlertOwner(principal);

        if (allAlertsByAlertOwner.size() > 1) {
            throw new AlertAmountException("Alert amount exceeded.");
        }

        // checks if alert with provided triggering price already exists
        boolean matched = allAlertsByAlertOwner.stream()
                .anyMatch(user -> user.getAlertTriggeringPrice().equals(alert.getAlertTriggeringPrice()));

        // if alert exists throw and error
        if (matched) {
            throw new AlertAlreadyExistsException("Alert with price '" + alert.getAlertTriggeringPrice() + "' already exists.");
        }

        // else save to the db and return dto
        userRepository.findByUsername(principal.getName()).ifPresent(user -> {
            alert.setAlertOwner(user.getUsername());
            alert.setUser(user);
        });

        return new ResponseEntity<>(alertMapper.alertToDto(alertRepository.save(alert)), HttpStatus.CREATED);
    }

    public List<AlertDto> getAllAlertsByAlertOwner(Principal alertOwner) {
        // returns all alerts but without some not necessary properties
        return alertRepository.findAllByAlertOwner(alertOwner.getName())
                .stream()
                .map(alertMapper::alertToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> deleteAlert(String alertId, Principal principal) {

        Optional<Alert> optionalAlert = alertRepository.findByIdAndAlertOwner(Long.valueOf(alertId), principal.getName());

        if (!optionalAlert.isPresent()) {
            throw new AlertIdException("Alert with id '" + alertId + "' does not exists on this account.");
        }

        optionalAlert.ifPresent(alertRepository::delete);

        return new ResponseEntity<String>("Alert wth id '" + alertId + "' was deleted successfully.", HttpStatus.OK);
    }
}
