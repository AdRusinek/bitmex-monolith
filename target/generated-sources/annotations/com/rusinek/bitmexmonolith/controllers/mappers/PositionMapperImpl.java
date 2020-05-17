package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.response.PositionDto;
import com.rusinek.bitmexmonolith.dto.response.PositionDto.PositionDtoBuilder;
import com.rusinek.bitmexmonolith.model.response.Position;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-15T13:26:55+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class PositionMapperImpl implements PositionMapper {

    @Override
    public List<PositionDto> positionsToDto(List<Position> position) {
        if ( position == null ) {
            return null;
        }

        List<PositionDto> list = new ArrayList<PositionDto>( position.size() );
        for ( Position position1 : position ) {
            list.add( positionToPositionDto( position1 ) );
        }

        return list;
    }

    protected PositionDto positionToPositionDto(Position position) {
        if ( position == null ) {
            return null;
        }

        PositionDtoBuilder positionDto = PositionDto.builder();

        positionDto.openingTimestamp( position.getOpeningTimestamp() );
        positionDto.symbol( position.getSymbol() );
        positionDto.openingQty( position.getOpeningQty() );
        positionDto.homeNotional( position.getHomeNotional() );
        positionDto.avgEntryPrice( position.getAvgEntryPrice() );
        positionDto.markPrice( position.getMarkPrice() );
        positionDto.liquidationPrice( position.getLiquidationPrice() );
        positionDto.leverage( position.getLeverage() );

        return positionDto.build();
    }
}
