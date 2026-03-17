package com.exchangerate.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "exchange_rates",
        indexes = {
                @Index(name = "idx_currency_date", columnList = "targetCurrency, rateDate")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames={"targetCurrency","rateDate"})
        }
)
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 3)
    private String baseCurrency;

    @Column(nullable = false, length = 3)
    private String targetCurrency;

    @Column(precision = 10, scale = 6)
    private BigDecimal cashBuy;

    @Column(precision = 10, scale = 6)
    private BigDecimal cashSell;

    @Column(precision = 10, scale = 6)
    private BigDecimal spotBuy;

    @Column(precision = 10, scale = 6)
    private BigDecimal spotSell;

    @Column(nullable = false)
    private LocalDate rateDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}