package com.rusinek.bitmexmonolith.util;

import com.rusinek.bitmexmonolith.dto.response.OrderStopDto;
import com.rusinek.bitmexmonolith.model.Stop;
import com.rusinek.bitmexmonolith.model.response.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Component
public class ResponseModifier {

    public List<Order> extractAndSetNewQuantity(List<Order> orders) {
        return orders.stream().peek(order -> {
            if (order.getSide().equals("Sell")) {
                order.setOrderQty(-order.getOrderQty());
            }
        }).collect(Collectors.toList());
    }

    public List<OrderStopDto> extractAndSetNewOrderPrice(List<OrderStopDto> orderStopDtos) {
        return orderStopDtos.stream().peek(orderStopDto -> {
            if (orderStopDto.getPrice() == null) {
                orderStopDto.setPrice("Market");
            }
        }).collect(Collectors.toList());
    }

    public String setExecutionInstructionsToClient(Stop stop) {

        String closeOnTriggerInString;
        if (stop.getCloseOnTrigger()) {
            closeOnTriggerInString = ", Close";
        } else {
            closeOnTriggerInString = "";
        }
        return stop.getExecInst() + closeOnTriggerInString;
    }

    public String extractInstructions(Stop stop) {
        String finalInstructions = "";

        //if you only set Mark without close on trigger the exactInst on BitMEX is empty
        if (stop.getExecInst().equals("MarkPrice") && !stop.getCloseOnTrigger()) {
            finalInstructions = "";
        }
        //if you set Mark with close on trigger with close on trigger then on BitMEX exactInst is "Close"
        if (stop.getExecInst().equals("MarkPrice") && stop.getCloseOnTrigger()) {
            finalInstructions = "Close";
        }
        if (stop.getCloseOnTrigger()) {
            finalInstructions = "Close, " + stop.getExecInst();
        }
        if (!stop.getCloseOnTrigger()) {
            finalInstructions = stop.getExecInst();
        }
        return finalInstructions;
    }
}
