package com.rusinek.bitmexmonolith.services.positions;

import com.rusinek.bitmexmonolith.services.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rusinek.bitmexmonolith.services.exchange.ExchangeService.HTTP_METHOD.GET;

/**
 * Created by Adrian Rusinek on 23.02.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final ExchangeService exchangeService;

    @Override
    public ResponseEntity<String> requestPositions(String accountId, Principal principal) {
        Map<String, Object> params = new HashMap<>();
        try {
            @SuppressWarnings("unchecked")
            List<Map> positionList = (List<Map>) exchangeService.requestApi(GET, "/position", params, Long.valueOf(accountId), principal.getName());
            if (positionList == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            JSONArray array = new JSONArray();
            array.put(positionList.get(0));

            for (int i = 0; i < array.length(); ++i) {
                JSONObject rec = array.getJSONObject(i);
                int currentQty = rec.getInt("currentQty");
                if (currentQty == 0) {
                    return new ResponseEntity<>(new JSONArray().toString(), HttpStatus.OK);
                }
            }

            return new ResponseEntity<>(array.toString(), HttpStatus.OK);
        } catch (ClassCastException ex) {
            log.error("Failure requesting position from BitMEX.");
            ex.getMessage();
        }
        return new ResponseEntity<>(new JSONArray().toString(), HttpStatus.ACCEPTED);
    }
}
