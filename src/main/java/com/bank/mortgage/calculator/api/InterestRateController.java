package com.bank.mortgage.calculator.api;

import com.bank.mortgage.calculator.domain.MortgageInterestRateResponse;
import com.bank.mortgage.calculator.service.InterestRateService;
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

    private InterestRateService interestRateService;

    @InterestRateApiDocumentation
    @GetMapping("/api/interest-rates")
    public ResponseEntity<List<MortgageInterestRateResponse>> getInterestRates() {
        log.debug("Received request to get interest rates");
        return ResponseEntity.ok().body(interestRateService.getInterestRates());
    }
}
