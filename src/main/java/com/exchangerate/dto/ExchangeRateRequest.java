package com.exchangerate.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeRateRequest {

    private String currency;

    private BigDecimal cashBuy;
    private BigDecimal cashSell;

    private BigDecimal spotBuy;
    private BigDecimal spotSell;

    private LocalDate rateDate;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getCashBuy() {
        return cashBuy;
    }

    public void setCashBuy(BigDecimal cashBuy) {
        this.cashBuy = cashBuy;
    }

    public BigDecimal getCashSell() {
        return cashSell;
    }

    public void setCashSell(BigDecimal cashSell) {
        this.cashSell = cashSell;
    }

    public BigDecimal getSpotBuy() {
        return spotBuy;
    }

    public void setSpotBuy(BigDecimal spotBuy) {
        this.spotBuy = spotBuy;
    }

    public BigDecimal getSpotSell() {
        return spotSell;
    }

    public void setSpotSell(BigDecimal spotSell) {
        this.spotSell = spotSell;
    }

    public LocalDate getRateDate() {
        return rateDate;
    }

    public void setRateDate(LocalDate rateDate) {
        this.rateDate = rateDate;
    }
}