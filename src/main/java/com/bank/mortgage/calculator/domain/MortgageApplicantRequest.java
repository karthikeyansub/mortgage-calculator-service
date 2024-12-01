package com.bank.mortgage.calculator.domain;

import com.bank.mortgage.calculator.exception.InvalidInputException;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MortgageApplicantRequest(BigDecimal income,
                                       Integer maturityPeriod,
                                       BigDecimal loanValue,
                                       BigDecimal homeValue) {

    public void validateFields() {
        if(this.income == null) throw new InvalidInputException("Income value cannot be null");
        if(this.maturityPeriod == null) throw new InvalidInputException("Maturity period value cannot be null");
        if(this.loanValue == null) throw new InvalidInputException("Loan value cannot be null");
        if(this.homeValue == null) throw new InvalidInputException("Home value cannot be null");
    }
}
