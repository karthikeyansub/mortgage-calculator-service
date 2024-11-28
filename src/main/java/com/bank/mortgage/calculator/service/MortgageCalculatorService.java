package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.MortgageApplicantRequest;
import com.bank.mortgage.calculator.domain.MortgageApplicantResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
@Slf4j
@AllArgsConstructor
public class MortgageCalculatorService {

    private InterestRateCacheService interestRateCacheService;

    public MortgageApplicantResponse mortageEligibilityCheck(final MortgageApplicantRequest mortgageApplicantRequest) {

        if(mortgageApplicantRequest.loanValue().compareTo(mortgageApplicantRequest.homeValue()) > 0) {
            log.info("Applicant loan value exceeds the home value");
            return new MortgageApplicantResponse(false, null);
        }

        BigDecimal eligibleAmount = mortgageApplicantRequest.income().multiply(new BigDecimal(4));

        if(mortgageApplicantRequest.loanValue().compareTo(eligibleAmount) > 0) {
            log.info("Applicant requested loan amount exceeds the eligible loan amount");
            return new MortgageApplicantResponse(false, null);
        }

        BigDecimal monthlyPayment  = calculateMonthlyAmount(mortgageApplicantRequest);
        log.info("Eligible for mortgage loan and monthly payment amount is {}", monthlyPayment);


        return new MortgageApplicantResponse(true, monthlyPayment);
    }

    private BigDecimal calculateMonthlyAmount(final MortgageApplicantRequest mortgageApplicantRequest) {
        //TODO: get interest rate for the particular maturityPeriod
        double interestRate = interestRateCacheService.getInterestRates().get(0).getInterestRate();

        double monthlyInterestRate = interestRate / 12;
        int totalPaymentTerms = mortgageApplicantRequest.maturityPeriod() * 12;

        double numerator = monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalPaymentTerms);
        double denominator = Math.pow(1 + monthlyInterestRate, totalPaymentTerms) - 1;

        double monthlyPayment = mortgageApplicantRequest.loanValue().doubleValue() * numerator / denominator;
        return new BigDecimal(monthlyPayment, MathContext.DECIMAL64);
    }

}
