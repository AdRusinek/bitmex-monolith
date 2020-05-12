package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.response.PositionDto;
import com.rusinek.bitmexmonolith.model.response.Position;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Mapper
public interface PositionMapper {

    List<PositionDto> positionsToDto(List<Position> position);
}
