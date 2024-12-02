package com.bank.mortgage.calculator.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public record MortgageInterestRate(int maturityPeriod,
                                   double interestRate,
                                   LocalDateTime lastUpdate) implements Serializable {
}
