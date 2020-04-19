package com.rusinek.bitmexmonolith.services.alerts;


import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.model.Alert;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.AlertRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
@Service
public class AlertServiceImpl implements AlertService{

    private AlertRepository alertRepository;
    private MapValidationErrorService errorService;
    private UserRepository userRepository;

    public AlertServiceImpl(AlertRepository alertRepository, MapValidationErrorService errorService, UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.errorService = errorService;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> saveAlertToAccount(Alert alert, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = errorService.validateErrors(result);
        if (errorMap != null) return errorMap;

        if (!getAllAlerts(principal).contains(alert)) {
            User user = userRepository.findByUsername(principal.getName());
            alert.setAlertOwner(user.getUsername());
            alert.setUser(user);

            return new ResponseEntity<>(alertRepository.save(alert), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Alert already exists.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<Alert> getAllAlerts(Principal alertOwner) {
        return (List<Alert>) alertRepository.findAllByAlertOwner(alertOwner.getName());
    }

    @Override
    public Iterable<Alert> findAll() {
        return alertRepository.findAll();
    }

    @Override
    public void delete(Alert alert) {
        alertRepository.delete(alert);
    }
}
