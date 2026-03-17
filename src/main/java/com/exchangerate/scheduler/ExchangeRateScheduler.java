package com.exchangerate.scheduler;

import com.exchangerate.service.TaiwanBankRateFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateScheduler {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateScheduler.class);

    private final TaiwanBankRateFetchService taiwanBankRateFetchService;

    public ExchangeRateScheduler(TaiwanBankRateFetchService taiwanBankRateFetchService) {
        this.taiwanBankRateFetchService = taiwanBankRateFetchService;
    }

    @Scheduled(cron = "${app.scheduler.exchange-rate.cron}", zone = "Asia/Taipei")
    public void fetchExchangeRates() {
        log.info("Scheduled task: fetch exchange rates from Taiwan Bank");
        taiwanBankRateFetchService.fetchAndSaveRates();
    }
}
