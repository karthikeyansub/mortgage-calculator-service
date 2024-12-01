package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.MortgageInterestRateResponse;
import com.bank.mortgage.calculator.domain.Entity.MortgageInterestRate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class InterestRateService {

    private final InterestRateCacheService interestRateCacheService;

    public List<MortgageInterestRateResponse> getInterestRates() {

        // Handled interest rate collection null or empty safely and transformed to a response object.
        return Stream.ofNullable(interestRateCacheService.getInterestRates())
                .filter(interestRates -> !interestRates.isEmpty())
                .flatMap(Collection::stream)
                .map(mortgageInterestRateMapperFunction)
                .toList();
    }

    private final Function<MortgageInterestRate, MortgageInterestRateResponse> mortgageInterestRateMapperFunction = mortgageInterestRate ->
            new MortgageInterestRateResponse(mortgageInterestRate.getMaturityPeriod(), mortgageInterestRate.getInterestRate(), mortgageInterestRate.getLastUpdate());

}
