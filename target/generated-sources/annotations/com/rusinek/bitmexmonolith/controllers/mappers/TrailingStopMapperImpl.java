package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.TrailingStopDto;
import com.rusinek.bitmexmonolith.dto.TrailingStopDto.TrailingStopDtoBuilder;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-04T03:47:38+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
@Component
public class TrailingStopMapperImpl implements TrailingStopMapper {

    @Override
    public TrailingStopDto trailingStopToDto(TrailingStop trailingStop) {
        if ( trailingStop == null ) {
            return null;
        }

        TrailingStopDtoBuilder trailingStopDto = TrailingStopDto.builder();

        trailingStopDto.id( trailingStop.getId() );
        trailingStopDto.startingPrice( trailingStop.getStartingPrice() );
        trailingStopDto.quantity( trailingStop.getQuantity() );
        trailingStopDto.trialValue( trailingStop.getTrialValue() );
        trailingStopDto.execInst( trailingStop.getExecInst() );

        return trailingStopDto.build();
    }
}
