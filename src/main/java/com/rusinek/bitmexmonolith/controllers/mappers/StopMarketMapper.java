package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.StopMarketDto;
import com.rusinek.bitmexmonolith.model.StopMarket;
import com.rusinek.bitmexmonolith.util.ResponseModifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@Component
@RequiredArgsConstructor
public class StopMarketMapper {

    private final ResponseModifier responseModifier;

    public StopMarketDto stopMarketToDto(StopMarket stopMarket) {
        if (stopMarket == null) {
            return null;
        }

        return StopMarketDto.builder()
                .id(stopMarket.getId())
                .accountId(stopMarket.getAccount().getId())
                .startingPrice(stopMarket.getStartingPrice())
                .quantity(stopMarket.getQuantity())
                .stopPrice(stopMarket.getStopPrice())
                .execInst(responseModifier.setExecutionInstructionsToClient(stopMarket))
                .build();
    }
}
