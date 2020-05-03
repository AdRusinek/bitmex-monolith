package com.rusinek.bitmexmonolith.services;


import com.rusinek.bitmexmonolith.controllers.mappers.AlertMapper;
import com.rusinek.bitmexmonolith.dto.AlertDto;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.model.Alert;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.AlertRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;
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

        if(result.hasErrors()) return errorService.validateErrors(result);

        if (!getAllAlerts(principal).contains(alert)) {
            User user = userRepository.findByUsername(principal.getName());
            alert.setAlertOwner(user.getUsername());
            alert.setUser(user);

            return new ResponseEntity<>(alertRepository.save(alert), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Alert already exists.", HttpStatus.BAD_REQUEST);
    }

    public List<AlertDto> getAllAlerts(Principal alertOwner) {
        return alertRepository.findAllByAlertOwner(alertOwner.getName()).stream()
                .map(alertMapper::alertToDto).collect(Collectors.toList());
    }

    public Iterable<Alert> findAll() {
        return alertRepository.findAll();
    }

    public void delete(Alert alert) {
        alertRepository.delete(alert);
    }
}
