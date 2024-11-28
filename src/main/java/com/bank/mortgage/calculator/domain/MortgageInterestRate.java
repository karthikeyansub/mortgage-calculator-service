package com.bank.mortgage.calculator.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "MORTGAGE_INTEREST_RATE")
public class MortgageInterestRate {

    @Id
    @Column(name = "maturity_period", nullable = false)
    private Integer maturityPeriod;

    @Column(name = "interest_rate", nullable = false)
    private double interestRate;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
}
