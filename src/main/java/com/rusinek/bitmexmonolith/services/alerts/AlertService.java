package com.rusinek.bitmexmonolith.services.alerts;

import com.rusinek.bitmexmonolith.dto.AlertDto;
import com.rusinek.bitmexmonolith.model.Alert;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
public interface AlertService {

   ResponseEntity<?> saveAlertToAccount(Alert alert, BindingResult result, Principal principal);

   List<AlertDto> getAllAlerts(Principal alertOwner);

   Iterable<Alert> findAll();

   void delete(Alert alert);
}
