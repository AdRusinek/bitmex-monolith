package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.AlertDto;
import com.rusinek.bitmexmonolith.model.Alert;
import org.mapstruct.Mapper;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
@Mapper
public interface AlertMapper {

    AlertDto alertToDto(Alert alert);
}
