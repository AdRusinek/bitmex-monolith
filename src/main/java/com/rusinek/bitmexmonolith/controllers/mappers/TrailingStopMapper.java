package com.rusinek.bitmexmonolith.controllers.mappers;

import com.rusinek.bitmexmonolith.dto.TrailingStopDto;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import com.rusinek.bitmexmonolith.util.ResponseModifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created by Adrian Rusinek on 04.05.2020
 **/
@Component
@RequiredArgsConstructor
public class TrailingStopMapper {

    private final ResponseModifier responseModifier;

    public TrailingStopDto trailingStopToDto(TrailingStop trailingStop) {
        if (trailingStop == null) {
            return null;
        }

        return TrailingStopDto.builder()
                .id(trailingStop.getId())
                .accountId(trailingStop.getAccount().getId())
                .startingPrice(trailingStop.getStartingPrice())
                .quantity(trailingStop.getQuantity())
                .trialValue(trailingStop.getTrialValue())
                .execInst(responseModifier.setExecutionInstructionsToClient(trailingStop))
                .build();
    }
}
