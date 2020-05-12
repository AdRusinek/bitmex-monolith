package com.rusinek.bitmexmonolith.services.exchange;

import com.rusinek.bitmexmonolith.dto.response.OrderStopDto;
import com.rusinek.bitmexmonolith.model.response.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Component
public class ResponseModifier {

    List<Order> extractAndSetNewQuantity(List<Order> orders) {
        return orders.stream().peek(order -> {
            if (order.getSide().equals("Sell")) {
                order.setOrderQty(-order.getOrderQty());
            }
        }).collect(Collectors.toList());
    }

    List<OrderStopDto> extractAndSetNewOrderPrice(List<OrderStopDto> orderStopDtos) {
        return orderStopDtos.stream().peek(orderStopDto -> {
            if (orderStopDto.getPrice() == null) {
                orderStopDto.setPrice("Market");
            }
        }).collect(Collectors.toList());
    }
}
