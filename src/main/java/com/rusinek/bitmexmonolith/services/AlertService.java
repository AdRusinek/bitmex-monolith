package com.rusinek.bitmexmonolith.services;


import com.rusinek.bitmexmonolith.exceptions.alertException.AlertAlreadyExistsException;
import com.rusinek.bitmexmonolith.exceptions.alertException.AlertAmountException;
import com.rusinek.bitmexmonolith.exceptions.alertException.AlertIdException;
import com.rusinek.bitmexmonolith.model.Alert;
import com.rusinek.bitmexmonolith.repositories.AlertRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserRepository userRepository;

    public Alert saveAlertToAccount(Alert alert, Principal principal) {

        List<Alert> allAlertsByAlertOwner = getAllAlertsByAlertOwner(principal);

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

        return alertRepository.save(alert);
    }

    public List<Alert> getAllAlertsByAlertOwner(Principal alertOwner) {

        return alertRepository.findAllByAlertOwner(alertOwner.getName());
    }

    public void deleteAlert(String alertId, Principal principal) {

        Optional<Alert> optionalAlert = alertRepository.findByIdAndAlertOwner(Long.valueOf(alertId), principal.getName());

        if (!optionalAlert.isPresent()) {
            throw new AlertIdException("Alert with id '" + alertId + "' does not exists on this account.");
        }

        optionalAlert.ifPresent(alertRepository::delete);
    }
}
