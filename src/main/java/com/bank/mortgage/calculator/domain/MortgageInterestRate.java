package com.bank.mortgage.calculator.domain;

import java.time.LocalDateTime;

public record MortgageInterestRate(int maturityPeriod,
                                   double interestRate,
                                   LocalDateTime lastUpdate) {
}
