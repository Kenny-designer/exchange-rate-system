package com.exchangerate.service;

import com.exchangerate.entity.ExchangeRate;
import com.exchangerate.repository.ExchangeRateRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class TaiwanBankRateFetchService {

    private static final Logger log = LoggerFactory.getLogger(TaiwanBankRateFetchService.class);

    private static final String RATE_URL = "https://rate.bot.com.tw/xrt?Lang=zh-TW";
    private static final String BASE_CURRENCY = "TWD";

    private final ExchangeRateRepository repository;

    public TaiwanBankRateFetchService(ExchangeRateRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void fetchAndSaveRates() {

        try {

            Document doc = Jsoup.connect(RATE_URL)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Elements rows = doc.select("table tbody tr");

            for (Element row : rows) {

                Elements cells = row.select("td");

                if (cells.size() < 5) {
                    continue;
                }

                String currency = parseCurrency(row);

                if (currency == null || currency.length() > 3) {
                    continue;
                }

                BigDecimal cashBuy = parseRate(cells.get(1).text());
                BigDecimal cashSell = parseRate(cells.get(2).text());
                BigDecimal spotBuy = parseRate(cells.get(3).text());
                BigDecimal spotSell = parseRate(cells.get(4).text());


                LocalDate rateDate = LocalDate.now();

                Optional<ExchangeRate> existing =
                        repository.findByTargetCurrencyAndRateDate(currency, rateDate);

                ExchangeRate entity;

                if (existing.isPresent()) {

                    entity = existing.get();

                    entity.setCashBuy(cashBuy);
                    entity.setCashSell(cashSell);
                    entity.setSpotBuy(spotBuy);
                    entity.setSpotSell(spotSell);

                } else {

                    entity = new ExchangeRate();

                    entity.setBaseCurrency(BASE_CURRENCY);
                    entity.setTargetCurrency(currency);
                    entity.setRateDate(LocalDate.now());

                    entity.setCashBuy(cashBuy);
                    entity.setCashSell(cashSell);
                    entity.setSpotBuy(spotBuy);
                    entity.setSpotSell(spotSell);
                }

                repository.save(entity);

                log.info("Saved {}", currency);
            }

        } catch (Exception e) {

            log.error("Fetch exchange rate failed", e);

        }
    }

    private String parseCurrency(Element row) {

        Element link = row.selectFirst("a[href*=/xrt/history/]");

        if (link == null) return null;

        String href = link.attr("href");

        String code = href.substring(href.lastIndexOf("/") + 1).trim();

        return code.split("\\?")[0].trim();
    }

    private BigDecimal parseRate(String text) {

        if (text == null || text.equals("-") || text.isBlank()) {
            return null;
        }

        try {
            return new BigDecimal(text.replace(",", ""));
        } catch (Exception e) {
            return null;
        }
    }

}