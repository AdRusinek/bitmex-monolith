package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.controllers.mappers.AlertMapper;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.model.Alert;
import com.rusinek.bitmexmonolith.services.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;
    private final AlertMapper alertMapper;
    private final MapValidationErrorService errorService;

    @PostMapping("/set-alert")
    public ResponseEntity<?> postAlert(@RequestBody @Valid Alert alert, BindingResult result, Principal principal) {

        if (result.hasErrors()) {
            return errorService.validateErrors(result);
        }

        return new ResponseEntity<>(alertMapper.alertToDto(alertService.saveAlertToAccount(alert, principal)), HttpStatus.CREATED);
    }

    @GetMapping("/get-alerts")
    public Iterable<?> getAlerts(Principal principal) {
        return alertService.getAllAlertsByAlertOwner(principal)
                .stream()
                .map(alertMapper::alertToDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{alertId}")
    public ResponseEntity<?> deleteAlert(@PathVariable String alertId, Principal principal) {

        alertService.deleteAlert(alertId, principal);

        return new ResponseEntity<>("Alert wth id '" + alertId + "' was deleted successfully.", HttpStatus.OK);
    }
}
