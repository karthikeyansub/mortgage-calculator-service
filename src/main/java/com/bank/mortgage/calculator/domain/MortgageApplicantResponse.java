package com.bank.mortgage.calculator.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MortgageApplicantResponse(boolean feasible,
                                        BigDecimal costs,
                                        String message) {
}
