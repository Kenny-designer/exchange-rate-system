package com.exchangerate.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateScheduler {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateScheduler.class);

    @Scheduled(cron = "${app.scheduler.exchange-rate.cron:0 0 * * * *}") // default: every hour
    public void fetchExchangeRates() {
        log.info("Scheduled task: fetch exchange rates");
        // Add exchange rate fetch logic here
    }
}
