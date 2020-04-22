package com.rusinek.bitmexmonolith.controllers;


import com.rusinek.bitmexmonolith.model.Alert;
import com.rusinek.bitmexmonolith.services.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    @PostMapping("/set-alert")
    public ResponseEntity<?> postAlert(@RequestBody @Valid Alert alert,
                                      BindingResult result,
                                      Principal principal) {
        return alertService.saveAlertToAccount(alert,result, principal);
    }

    @GetMapping("/get-alerts")
    public Iterable<?> getAlerts(Principal principal) {
        return alertService.getAllAlerts(principal);
    }
}
