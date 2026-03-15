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
import java.util.Optional;
import java.util.Set;

@Service
public class TaiwanBankRateFetchService {

    private static final Logger log = LoggerFactory.getLogger(TaiwanBankRateFetchService.class);
    private static final String RATE_URL = "https://rate.bot.com.tw/xrt?Lang=zh-TW";
    private static final String BASE_CURRENCY = "TWD";
    private static final Set<String> TARGET_CURRENCIES = Set.of("USD", "JPY", "EUR");

    private final ExchangeRateRepository exchangeRateRepository;

    public TaiwanBankRateFetchService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    /**
     * Fetches USD, JPY, EUR rates from Taiwan Bank and saves to database.
     * Uses spot sell rate (即期匯率 本行賣出) as the rate (TWD per 1 unit of foreign currency).
     */
    @Transactional
    public void fetchAndSaveRates() {
        try {
            Document doc = Jsoup.connect(RATE_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10_000)
                    .get();

            Element table = doc.select("table[title=牌告匯率]").first();
            if (table == null) {
                log.warn("Exchange rate table not found on page");
                return;
            }

            Elements rows = table.select("tbody tr");
            for (Element row : rows) {
                String currency = parseCurrencyFromRow(row);
                if (currency == null || !TARGET_CURRENCIES.contains(currency)) {
                    continue;
                }

                Optional<BigDecimal> spotSell = parseSpotSellRate(row);
                if (spotSell.isEmpty()) {
                    log.warn("Could not parse spot sell rate for {}", currency);
                    continue;
                }

                ExchangeRate entity = new ExchangeRate();
                entity.setBaseCurrency(BASE_CURRENCY);
                entity.setTargetCurrency(currency);
                entity.setRate(spotSell.get());
                exchangeRateRepository.save(entity);
                log.info("Saved rate {} / {} = {}", BASE_CURRENCY, currency, spotSell.get());
            }
        } catch (Exception e) {
            log.error("Failed to fetch or save exchange rates from Taiwan Bank", e);
            throw new RuntimeException("Exchange rate fetch failed", e);
        }
    }

    private String parseCurrencyFromRow(Element row) {
        // Link to history page contains currency code: e.g. /xrt/history/USD
        Element link = row.selectFirst("a[href*=/xrt/history/]");
        if (link != null) {
            String href = link.attr("href");
            int lastSlash = href.lastIndexOf('/');
            if (lastSlash >= 0 && lastSlash < href.length() - 1) {
                return href.substring(lastSlash + 1).split("\\?")[0].trim();
            }
        }
        return null;
    }

    /**
     * Spot sell is the 4th rate column: 現金買入, 現金賣出, 即期買入, 即期賣出 (index 3 in 0-based).
     */
    private Optional<BigDecimal> parseSpotSellRate(Element row) {
        Elements cells = row.select("td");
        int rateIndex = 0;
        for (Element td : cells) {
            String text = td.text().trim();
            if (text.isEmpty() || text.equals("-")) {
                continue;
            }
            if (isNumeric(text)) {
                if (rateIndex == 3) {
                    return parseRate(text);
                }
                rateIndex++;
            }
        }
        return Optional.empty();
    }

    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(s.replace(",", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Optional<BigDecimal> parseRate(String s) {
        try {
            String cleaned = s.replace(",", "").trim();
            return Optional.of(new BigDecimal(cleaned).setScale(6, RoundingMode.HALF_UP));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
