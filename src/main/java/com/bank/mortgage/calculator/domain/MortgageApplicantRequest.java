package com.bank.mortgage.calculator.domain;

import java.math.BigDecimal;

public record MortgageApplicantRequest(BigDecimal income,
                                       Integer maturityPeriod,
                                       BigDecimal loanValue,
                                       BigDecimal homeValue) {
}
