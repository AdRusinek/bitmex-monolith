package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.response.OrderLimitDto;
import com.rusinek.bitmexmonolith.dto.response.OrderLimitDto.OrderLimitDtoBuilder;
import com.rusinek.bitmexmonolith.model.response.Order;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-12T02:57:34+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
@Component
public class OrderLimitMapperImpl implements OrderLimitMapper {

    @Override
    public List<OrderLimitDto> orderLimitToDto(List<Order> position) {
        if ( position == null ) {
            return null;
        }

        List<OrderLimitDto> list = new ArrayList<OrderLimitDto>( position.size() );
        for ( Order order : position ) {
            list.add( orderToOrderLimitDto( order ) );
        }

        return list;
    }

    protected OrderLimitDto orderToOrderLimitDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderLimitDtoBuilder orderLimitDto = OrderLimitDto.builder();

        orderLimitDto.timestamp( order.getTimestamp() );
        orderLimitDto.symbol( order.getSymbol() );
        orderLimitDto.orderQty( order.getOrderQty() );
        orderLimitDto.price( order.getPrice() );
        orderLimitDto.leavesQty( order.getLeavesQty() );

        return orderLimitDto.build();
    }
}
