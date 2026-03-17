package com.exchangerate.service;

import com.exchangerate.dto.ExchangeRateRequest;
import com.exchangerate.dto.ExchangeRateResponse;
import org.springframework.stereotype.Service;
import com.exchangerate.repository.ExchangeRateRepository;
import com.exchangerate.entity.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository repository;

    public ExchangeRateService(ExchangeRateRepository repository) {
        this.repository = repository;
    }

    public List<ExchangeRateResponse> getLatestRates(String currency) {
        List<ExchangeRate> existing =
                repository.findTop7ByTargetCurrencyOrderByCreatedAtDesc(currency);

        return existing.stream().map(exchangeRate -> {
            ExchangeRateResponse res = new ExchangeRateResponse();
            res.setBaseCurrency(exchangeRate.getBaseCurrency());
            res.setTargetCurrency(exchangeRate.getTargetCurrency());
            res.setCashBuy(exchangeRate.getCashBuy());
            res.setCashSell(exchangeRate.getCashSell());
            res.setSpotBuy(exchangeRate.getSpotBuy());
            res.setSpotSell(exchangeRate.getSpotSell());
            res.setRateDate(exchangeRate.getRateDate());
            return res;
        }).toList();
    }

    public List<ExchangeRate> getHistory(String currency) {
        return repository.findByTargetCurrencyOrderByRateDateAsc(currency);
    }

    public List<String> getCurrencies() {
        return repository.findDistinctCurrencies();
    }

    public List<ExchangeRate> getHistory(String currency, int days) {

        LocalDate startDate = LocalDate.now().minusDays(days -1);

        return repository.findHistory(currency, startDate);
    }

    public ExchangeRate saveRate(ExchangeRateRequest request) {

        String currency = request.getCurrency();
        if ( currency == null ) {
            currency = "USD"; // 預設使用 USD 查詢
        }
        LocalDate rateDate = request.getRateDate();
        if ( rateDate == null ) {
            rateDate = LocalDate.now(); // 預設使用當日查詢
        }

        Optional<ExchangeRate> existing =
                repository.findByTargetCurrencyAndRateDate(currency, rateDate);

        BigDecimal cashBuy = request.getCashBuy();
        BigDecimal cashSell = request.getCashSell();
        BigDecimal spotBuy = request.getSpotBuy();
        BigDecimal spotSell = request.getSpotSell();

        ExchangeRate entity;

        if (existing.isPresent()) {

            entity = existing.get();

            if (cashBuy != null) {
                entity.setCashBuy(cashBuy);
            }
            if (cashSell != null) {
                entity.setCashSell(cashSell);
            }
            if (spotBuy != null) {
                entity.setSpotBuy(spotBuy);
            }
            if (spotSell != null) {
                entity.setSpotSell(spotSell);
            }

        } else {

            entity = new ExchangeRate();

            entity.setBaseCurrency("TWD");
            entity.setTargetCurrency(currency);
            entity.setRateDate(rateDate);

            if (cashBuy != null) {
                entity.setCashBuy(cashBuy);
            }
            if (cashSell != null) {
                entity.setCashSell(cashSell);
            }
            if (spotBuy != null) {
                entity.setSpotBuy(spotBuy);
            }
            if (spotSell != null) {
                entity.setSpotSell(spotSell);
            }
        }

        return repository.save(entity);
    }
}
