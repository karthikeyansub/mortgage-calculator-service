package com.bank.mortgage.calculator.api;

import com.bank.mortgage.calculator.domain.MortgageApplicantRequest;
import com.bank.mortgage.calculator.domain.MortgageApplicantResponse;
import com.bank.mortgage.calculator.service.MortgageCalculatorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class MortgageCalculatorController {

    private final MortgageCalculatorService mortgageCalculatorService;

    @PostMapping("/api/mortgage-check")
    public MortgageApplicantResponse mortgageCalculator(@RequestBody MortgageApplicantRequest mortgageApplicantRequest) {
        return mortgageCalculatorService.mortageEligibilityCheck(mortgageApplicantRequest);
    }

}
