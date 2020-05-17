package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.AlertDto;
import com.rusinek.bitmexmonolith.dto.AlertDto.AlertDtoBuilder;
import com.rusinek.bitmexmonolith.model.Alert;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-15T13:26:55+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class AlertMapperImpl implements AlertMapper {

    @Override
    public AlertDto alertToDto(Alert alert) {
        if ( alert == null ) {
            return null;
        }

        AlertDtoBuilder alertDto = AlertDto.builder();

        alertDto.id( alert.getId() );
        alertDto.alertMessage( alert.getAlertMessage() );
        alertDto.alertTriggeringPrice( alert.getAlertTriggeringPrice() );

        return alertDto.build();
    }
}
