package com.bank.mortgage.calculator.api;

import com.bank.mortgage.calculator.domain.MortgageInterestRate;
import com.bank.mortgage.calculator.service.InterestRateCacheService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class InterestRateController {

    private InterestRateCacheService interestRateService;

    @InterestRateApiDocumentation
    @GetMapping("/api/interest-rates")
    public ResponseEntity<List<MortgageInterestRate>> getInterestRates() {
        log.info("Received request to get interest rates");
        return ResponseEntity.ok().body(interestRateService.getInterestRates());
    }
}
