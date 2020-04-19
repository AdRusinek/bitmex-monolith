package com.rusinek.bitmexmonolith.controllers;



import com.rusinek.bitmexmonolith.services.orders.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by Adrian Rusinek on 19.02.2020
 **/
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/get-orders/{status}/{type}/{accountId}")
    public ResponseEntity<?> getOrders(@PathVariable String status,
                                       @PathVariable String type,
                                       @PathVariable String accountId,
                                       Principal principal) {
        return orderService.requestOrders(status, type, accountId, principal);
    }
}
