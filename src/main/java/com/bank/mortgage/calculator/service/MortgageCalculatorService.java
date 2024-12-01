package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.MortgageApplicantRequest;
import com.bank.mortgage.calculator.domain.MortgageApplicantResponse;
import com.bank.mortgage.calculator.domain.Entity.MortgageInterestRate;
import com.bank.mortgage.calculator.exception.InvalidInputException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
@Slf4j
public class MortgageCalculatorService {

    private final InterestRateCacheService interestRateCacheService;

    public MortgageApplicantResponse mortageEligibilityCheck(final MortgageApplicantRequest mortgageApplicantRequest) {

        if(mortgageApplicantRequest.loanValue().compareTo(mortgageApplicantRequest.homeValue()) > 0) {
            log.debug("Applicant loan value exceeds the home value");
            return new MortgageApplicantResponse(false, null);
        }

        BigDecimal eligibleAmount = mortgageApplicantRequest.income().multiply(new BigDecimal(4));

        if(mortgageApplicantRequest.loanValue().compareTo(eligibleAmount) > 0) {
            log.debug("Applicant requested loan amount exceeds the eligible loan amount");
            return new MortgageApplicantResponse(false, null);
        }

        BigDecimal monthlyPayment  = calculateMonthlyAmount(mortgageApplicantRequest);
        log.debug("Eligible for mortgage loan and monthly payment amount is {}", monthlyPayment);

        return new MortgageApplicantResponse(true, monthlyPayment);
    }

    //TODO: Need to fix the monthly cost calculation
    private BigDecimal calculateMonthlyAmount(final MortgageApplicantRequest mortgageApplicantRequest) {
        MortgageInterestRate mortgageInterestRate = interestRateCacheService.getInterestRateByMaturityPeriod(mortgageApplicantRequest.maturityPeriod());

        double interestRate = null != mortgageInterestRate ? mortgageInterestRate.getInterestRate() : 0d;

        if(interestRate <= 0){
           throw new InvalidInputException(String.format("No interest rate found for the given maturity period(%d).", mortgageApplicantRequest.maturityPeriod()));
        }
        double monthlyInterestRate = interestRate / 12;
        int totalPaymentTerms = mortgageApplicantRequest.maturityPeriod() * 12;

        double numerator = monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalPaymentTerms);
        double denominator = Math.pow(1 + monthlyInterestRate, totalPaymentTerms) - 1;

        double monthlyPayment = mortgageApplicantRequest.loanValue().doubleValue() * (numerator / denominator);
        return new BigDecimal(monthlyPayment).setScale(2, RoundingMode.HALF_EVEN);
    }

}
