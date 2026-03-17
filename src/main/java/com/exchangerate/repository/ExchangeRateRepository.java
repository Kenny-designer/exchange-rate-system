package com.exchangerate.repository;

import com.exchangerate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findTop7ByTargetCurrencyOrderByCreatedAtDesc(String currency);

    List<ExchangeRate> findByTargetCurrencyOrderByRateDateAsc(String currency);

    @Query("SELECT DISTINCT e.targetCurrency FROM ExchangeRate e ORDER BY e.targetCurrency")
    List<String> findDistinctCurrencies();

    @Query("SELECT e FROM ExchangeRate e WHERE e.targetCurrency = :currency AND e.rateDate >= :startDate ORDER BY e.rateDate ASC")
    List<ExchangeRate> findHistory(String currency,LocalDate startDate);

    Optional<ExchangeRate> findByTargetCurrencyAndRateDate(String currency, LocalDate rateDate);


}
