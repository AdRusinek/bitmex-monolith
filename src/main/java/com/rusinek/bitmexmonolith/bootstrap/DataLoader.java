package com.rusinek.bitmexmonolith.bootstrap;

import com.rusinek.bitmexmonolith.model.limits.ExchangeRequestLimit;
import com.rusinek.bitmexmonolith.repositories.ExchangeRequestLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Adrian Rusinek on 26.05.2020
 **/
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final ExchangeRequestLimitRepository exchangeRequestLimitRepository;

    @SuppressWarnings("NullableProblems")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if(exchangeRequestLimitRepository.findAll().size() == 0) {

            ExchangeRequestLimit exchangeRequestLimit = new ExchangeRequestLimit();
            exchangeRequestLimit.setBlockadeActivatedAt(System.currentTimeMillis() / 1000L);
            exchangeRequestLimit.setApiReadyToUse(System.currentTimeMillis() / 1000L);

            exchangeRequestLimitRepository.save(exchangeRequestLimit);
        }
    }
}
