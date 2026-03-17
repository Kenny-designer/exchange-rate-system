package com.exchangerate.controller;

import com.exchangerate.dto.ExchangeRateResponse;
import org.springframework.web.bind.annotation.*;
import com.exchangerate.service.ExchangeRateService;
import com.exchangerate.entity.ExchangeRate;
import com.exchangerate.dto.ExchangeRateRequest;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {

    private final ExchangeRateService service;

    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    @GetMapping("/rates/{currency}")
    public List<ExchangeRateResponse> getRates(@PathVariable String currency) {
        return service.getLatestRates(currency);
    }

    @PostMapping("/update")
    public ExchangeRate updateRate(@RequestBody ExchangeRateRequest request) {

        return service.saveRate(request);
    }

    @GetMapping("/rates/history/{currency}")
    public List<ExchangeRate> getHistory(
            @PathVariable String currency,
            @RequestParam(defaultValue = "7") int days
    ) {
        return service.getHistory(currency, days);
    }

    @GetMapping("/currencies")
    public List<String> getCurrencies() {
        return service.getCurrencies();
    }
}