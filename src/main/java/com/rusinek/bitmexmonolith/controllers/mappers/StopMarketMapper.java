package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.StopMarketDto;
import com.rusinek.bitmexmonolith.model.StopMarket;
import org.springframework.stereotype.Component;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@Component
public class StopMarketMapper {

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
                .execInst(setExecutionInstructions(stopMarket))
                .build();
    }

    private String setExecutionInstructions(StopMarket stopMarket) {

        String closeOnTriggerInString;
        if (stopMarket.getCloseOnTrigger()) {
            closeOnTriggerInString = ", Close";
        } else {
            closeOnTriggerInString = "";
        }
        return stopMarket.getExecInst() + closeOnTriggerInString;
    }
}
