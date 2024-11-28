package com.bank.mortgage.calculator.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public record MortgageInterestRateResponse(Integer maturityPeriod,
                                           double interestRate,
                                           LocalDateTime lastUpdate) implements Serializable {
}
