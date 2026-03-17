package com.exchangerate.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeRateResponse {

    private String baseCurrency;

    private String targetCurrency;

    private BigDecimal cashBuy;

    private BigDecimal cashSell;

    private BigDecimal spotBuy;

    private BigDecimal spotSell;

    private LocalDate rateDate;

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public void setCashBuy(BigDecimal cashBuy) {
        this.cashBuy = cashBuy;
    }

    public void setCashSell(BigDecimal cashSell) {
        this.cashSell = cashSell;
    }

    public void setSpotBuy(BigDecimal spotBuy) {
        this.spotBuy = spotBuy;
    }

    public void setSpotSell(BigDecimal spotSell) {
        this.spotSell = spotSell;
    }

    public void setRateDate(LocalDate rateDate) {
        this.rateDate = rateDate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getCashBuy() {
        return cashBuy;
    }

    public BigDecimal getCashSell() {
        return cashSell;
    }

    public BigDecimal getSpotBuy() {
        return spotBuy;
    }

    public BigDecimal getSpotSell() {
        return spotSell;
    }

    public LocalDate getRateDate() {
        return rateDate;
    }
}
