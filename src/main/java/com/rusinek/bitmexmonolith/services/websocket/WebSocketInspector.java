package com.rusinek.bitmexmonolith.services.websocket;

import com.rusinek.bitmexmonolith.services.websocket.executors.AlertSender;
import com.rusinek.bitmexmonolith.services.websocket.executors.StopMarketSender;
import com.rusinek.bitmexmonolith.services.websocket.executors.TrailingStopSender;
import info.bitrich.xchangestream.bitmex.BitmexStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.exceptions.ExchangeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Adrian Rusinek on 22.03.2020
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketInspector implements ApplicationListener<ContextRefreshedEvent> {

    private final TrailingStopSender trailingStopSender;
    private final StopMarketSender stopMarketSender;
    private final AlertSender alertSender;
    @Value("${bitmex-monolith.use-sandbox}")
    private boolean useSandbox;

    @SuppressWarnings("NullableProblems")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Websocket started.");
        triggerActions();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void triggerActions() {
        StreamingExchange exchange = null;
        try {
            ExchangeSpecification specification = new ExchangeSpecification(BitmexStreamingExchange.class);
            specification.setExchangeSpecificParametersItem("UseSandbox", useSandbox);

            exchange = StreamingExchangeFactory.INSTANCE.createExchange(specification);
            exchange.connect().blockingAwait();
        } catch (ExchangeException e) {
            log.error("BitMEX is down.");
        }

        if (exchange != null) {
            exchange.getStreamingMarketDataService().getTrades(CurrencyPair.XBT_USD)
                    .subscribe(this::executeActions,
                            throwable -> log.error("Error in subscribing trades.", throwable));
        }
    }

    private void executeActions(Trade trade) {
        trailingStopSender.iterateTrailingStop(trade);
        stopMarketSender.iterateStopMarket(trade);
        alertSender.iterateAlert(trade);
    }
}
