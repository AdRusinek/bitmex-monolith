package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.response.OrderLimitDto;
import com.rusinek.bitmexmonolith.model.response.Order;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Mapper
public interface OrderLimitMapper {

    List<OrderLimitDto> orderLimitToDto(List<Order> position);
}
