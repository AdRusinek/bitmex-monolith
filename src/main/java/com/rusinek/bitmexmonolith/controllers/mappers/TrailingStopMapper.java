package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.TrailingStopDto;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import org.springframework.stereotype.Component;

/**
 * Created by Adrian Rusinek on 04.05.2020
 **/
@Component
public class TrailingStopMapper {

    public TrailingStopDto trailingStopToDto(TrailingStop trailingStop) {
        if (trailingStop == null) {
            return null;
        }

        return TrailingStopDto.builder()
                .id(trailingStop.getId())
                .startingPrice(trailingStop.getStartingPrice())
                .quantity(trailingStop.getQuantity())
                .trialValue(trailingStop.getTrialValue())
                .execInst(setExecutionInstructions(trailingStop))
                .build();
    }

    private String setExecutionInstructions(TrailingStop trailingStop) {

        String closeOnTriggerInString;
        if (trailingStop.getCloseOnTrigger()) {
            closeOnTriggerInString = ", Close";
        } else {
            closeOnTriggerInString = "";
        }
        return trailingStop.getExecInst() + closeOnTriggerInString;
    }
}
