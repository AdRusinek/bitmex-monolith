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
    date = "2020-05-13T07:31:49+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class OrderStopMapperImpl implements OrderStopMapper {

    @Override
    public List<OrderStopDto> orderStopsToDtos(List<Order> position) {
        if ( position == null ) {
            return null;
        }

        List<OrderStopDto> list = new ArrayList<OrderStopDto>( position.size() );
        for ( Order order : position ) {
            list.add( orderStopToDto( order ) );
        }

        return list;
    }

    @Override
    public OrderStopDto orderStopToDto(Order order) {
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
