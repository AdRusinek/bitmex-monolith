package com.rusinek.bitmexmonolith.util;

import com.google.gson.Gson;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Service
public class ParameterService {

    public enum RequestContent {
        GET_POSITIONS,
        GET_STOP_ORDERS,
        GET_LIMIT_ORDERS,
        GET_API_KEY,

        POST_TRAILING_STOP
    }

    public Map<String, Object> fillParamsForGetRequest(RequestContent requestContent) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> filterMap = new HashMap<>();
        Gson gson = new Gson();


        switch (requestContent) {
            case GET_API_KEY:
                params.put("reverse", false);
                return params;
            case GET_POSITIONS:
                filterMap.put("isOpen", true);
                filterMap.put("symbol", "XBTUSD");

                params.put("filter", gson.toJson(filterMap));
                return params;
            case GET_LIMIT_ORDERS:
                filterMap.put("symbol", "XBTUSD");
                filterMap.put("ordType", "Limit");
                filterMap.put("open", true);

                params.put("symbol", "XBT");
                params.put("filter", gson.toJson(filterMap));
                params.put("count", 20);
                params.put("reverse", false);
                return params;
            case GET_STOP_ORDERS:
                String[] ordTypes = {"Stop", "MarketIfTouched", "StopLimit", "LimitIfTouched"};
                filterMap.put("symbol", "XBTUSD");
                filterMap.put("ordType", ordTypes);
                filterMap.put("open", true);

                params.put("symbol", "XBT");
                params.put("filter", gson.toJson(filterMap));
                params.put("count", 20);
                params.put("reverse", false);
                return params;
        }
        return null;
    }

    public Map<String, Object> fillParamsForPostRequest(RequestContent requestContent, TrailingStop trailingStop, String exactInstructions) {

        Map<String, Object> params = new HashMap<>();

        // switch  for later for now could replace with if
        if (requestContent == RequestContent.POST_TRAILING_STOP) {
            params.put("symbol", "XBTUSD");
            params.put("ordType", "Stop");
            params.put("pegPriceType ", "TrailingStopPeg");
            params.put("pegOffsetValue", trailingStop.getTrialValue());
            params.put("orderQty", trailingStop.getQuantity());
            params.put("execInst", exactInstructions);
            return params;
        }
        return null;
    }
}
