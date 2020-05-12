package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.response.OrderStopDto;
import com.rusinek.bitmexmonolith.dto.response.OrderStopDto.OrderStopDtoBuilder;
import com.rusinek.bitmexmonolith.model.response.Order;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-12T03:05:07+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
@Component
public class OrderStopMapperImpl implements OrderStopMapper {

    @Override
    public List<OrderStopDto> orderStopToDto(List<Order> position) {
        if ( position == null ) {
            return null;
        }

        List<OrderStopDto> list = new ArrayList<OrderStopDto>( position.size() );
        for ( Order order : position ) {
            list.add( orderToOrderStopDto( order ) );
        }

        return list;
    }

    protected OrderStopDto orderToOrderStopDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderStopDtoBuilder orderStopDto = OrderStopDto.builder();

        orderStopDto.timestamp( order.getTimestamp() );
        orderStopDto.symbol( order.getSymbol() );
        orderStopDto.orderQty( order.getOrderQty() );
        if ( order.getPrice() != null ) {
            orderStopDto.price( String.valueOf( order.getPrice() ) );
        }

        return orderStopDto.build();
    }
}
