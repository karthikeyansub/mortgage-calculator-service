package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.MortgageApplicantRequest;
import com.bank.mortgage.calculator.domain.MortgageApplicantResponse;
import com.bank.mortgage.calculator.domain.MortgageInterestRate;
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
            return new MortgageApplicantResponse(false, null, "Loan value exceeds the home value");
        }

        BigDecimal eligibleAmount = mortgageApplicantRequest.income().multiply(new BigDecimal(4));

        if(mortgageApplicantRequest.loanValue().compareTo(eligibleAmount) > 0) {
            log.debug("Applicant requested loan amount exceeds 4 times the income amount");
            return new MortgageApplicantResponse(false, null, "Loan amount exceeds 4 times the income amount");
        }

        BigDecimal monthlyPayment  = calculateMonthlyAmount(mortgageApplicantRequest);
        log.debug("Eligible for mortgage loan and monthly payment amount is {}", monthlyPayment);

        return new MortgageApplicantResponse(true, monthlyPayment, null);
    }

    private BigDecimal calculateMonthlyAmount(final MortgageApplicantRequest mortgageApplicantRequest) {
        MortgageInterestRate mortgageInterestRate = interestRateCacheService.getInterestRateByMaturityPeriod(mortgageApplicantRequest.maturityPeriod());

        double interestRate = null != mortgageInterestRate ? mortgageInterestRate.interestRate() : 0d;

        if(interestRate <= 0){
           throw new InvalidInputException(String.format("No interest rate found for the given maturity period(%d).", mortgageApplicantRequest.maturityPeriod()));
        }
        log.debug("Annual interest rate: {}%", interestRate);
        double monthlyInterestRate = (interestRate / 100) / 12;
        log.debug("Monthly interest rate: {}", monthlyInterestRate);

        int totalPaymentTermsInMonths = mortgageApplicantRequest.maturityPeriod() * 12;
        log.debug("Total repayment terms in months: {}", totalPaymentTermsInMonths);

        double numerator = monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalPaymentTermsInMonths);
        double denominator = Math.pow(1 + monthlyInterestRate, totalPaymentTermsInMonths) - 1;

        double monthlyPayment = mortgageApplicantRequest.loanValue().doubleValue() * numerator / denominator;
        log.debug("Monthly payment amount before rounding off: {}", monthlyPayment);

        return new BigDecimal(monthlyPayment).setScale(2, RoundingMode.HALF_EVEN);
    }

}
