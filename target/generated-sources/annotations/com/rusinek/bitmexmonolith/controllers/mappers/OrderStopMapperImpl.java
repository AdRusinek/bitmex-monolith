package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.response.OrderStopDto;
import com.rusinek.bitmexmonolith.model.response.Order;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-03T21:16:51+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
@Component
public class OrderStopMapperImpl implements OrderStopMapper {

    @Override
    public List<OrderStopDto> orderStopsToDto(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderStopDto> list = new ArrayList<OrderStopDto>( orders.size() );
        for ( Order order : orders ) {
            list.add( orderStopToDto( order ) );
        }

        return list;
    }

    @Override
    public OrderStopDto orderStopToDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderStopDto orderStopDto = new OrderStopDto();

        orderStopDto.setSymbol( order.getSymbol() );
        orderStopDto.setOrderQty( order.getOrderQty() );
        orderStopDto.setTimestamp( order.getTimestamp() );
        if ( order.getPrice() != null ) {
            orderStopDto.setPrice( String.valueOf( order.getPrice() ) );
        }

        return orderStopDto;
    }
}
