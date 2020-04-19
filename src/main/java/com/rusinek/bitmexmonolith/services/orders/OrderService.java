package com.rusinek.bitmexmonolith.services.orders;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface OrderService {

    ResponseEntity<?> requestOrders(String status, String type, String accountId, Principal principal);
}
