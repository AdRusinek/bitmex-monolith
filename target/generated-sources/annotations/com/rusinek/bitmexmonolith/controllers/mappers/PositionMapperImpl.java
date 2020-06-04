package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.response.PositionDto;
import com.rusinek.bitmexmonolith.model.response.Position;
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

        PositionDto positionDto = new PositionDto();

        positionDto.setSymbol( position.getSymbol() );
        positionDto.setOpeningTimestamp( position.getOpeningTimestamp() );
        positionDto.setOpeningQty( position.getOpeningQty() );
        positionDto.setHomeNotional( position.getHomeNotional() );
        positionDto.setAvgEntryPrice( position.getAvgEntryPrice() );
        positionDto.setMarkPrice( position.getMarkPrice() );
        positionDto.setLiquidationPrice( position.getLiquidationPrice() );
        positionDto.setLeverage( position.getLeverage() );

        return positionDto;
    }
}
