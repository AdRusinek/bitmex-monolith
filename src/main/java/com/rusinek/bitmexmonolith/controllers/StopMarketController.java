package com.rusinek.bitmexmonolith.controllers;

import com.rusinek.bitmexmonolith.controllers.mappers.StopMarketMapper;
import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.model.StopMarket;
import com.rusinek.bitmexmonolith.services.StopMarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stop-market-orders")
public class StopMarketController {

    private final MapValidationErrorService errorService;
    private final StopMarketMapper stopMarketMapper;
    private final StopMarketService stopMarketService;

    @PostMapping("/set-stop-market/{accountId}")
    public ResponseEntity<?> setStopMarket(@Valid @RequestBody StopMarket stopMarket, BindingResult result, Principal principal,
                                           @PathVariable String accountId) {

        if (result.hasErrors()) {
            return errorService.validateErrors(result);
        }

        return new ResponseEntity<>(stopMarketMapper.stopMarketToDto(stopMarketService
                .saveStopMarket(stopMarket, principal, accountId)), HttpStatus.OK);
    }

    @GetMapping("/get-waiting-stop-markets/{accountId}")
    public ResponseEntity<?> getWaitingStopMarkets(Principal principal, @PathVariable String accountId) {

        return new ResponseEntity<>(stopMarketService.getAllByOwnerAndAccountId(principal, accountId)
                .stream().map(stopMarketMapper::stopMarketToDto), HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}/{stopMarketId}")
    public ResponseEntity<?> deleteStopMarket(@PathVariable String accountId, @PathVariable String stopMarketId, Principal principal) {

        stopMarketService.deleteStopMarket(accountId, stopMarketId, principal);

        return new ResponseEntity<>("Stop Market wth id '" + stopMarketId + "' was deleted successfully " +
                "from account " + accountId + ".", HttpStatus.OK);
    }
}
