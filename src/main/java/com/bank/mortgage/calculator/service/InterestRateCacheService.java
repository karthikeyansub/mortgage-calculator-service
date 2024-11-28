package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.MortgageInterestRate;
import com.bank.mortgage.calculator.repository.MortgageInterestRateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class InterestRateCacheService {

    private final MortgageInterestRateRepository mortgageInterestRateRepository;

    //TODO: Update cache service
    public List<MortgageInterestRate> getInterestRates() {
        return mortgageInterestRateRepository.findAll();
    }
}
