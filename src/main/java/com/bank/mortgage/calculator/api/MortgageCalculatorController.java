package com.bank.mortgage.calculator.api;

import com.bank.mortgage.calculator.domain.MortgageApplicantRequest;
import com.bank.mortgage.calculator.domain.MortgageApplicantResponse;
import com.bank.mortgage.calculator.service.MortgageCalculatorService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class MortgageCalculatorController {

    private final MortgageCalculatorService mortgageCalculatorService;

    @MortgageCalculatorApiDocumentation
    @PostMapping("/api/mortgage-check")
    public MortgageApplicantResponse mortgageCalculator(
            @RequestBody(description = "Applicant request for mortgage calculation", required = true)
            @org.springframework.web.bind.annotation.RequestBody final MortgageApplicantRequest mortgageApplicantRequest) {
        log.info("Received request for /api/mortgage-check - applicant details: {} ", mortgageApplicantRequest.toString());

        mortgageApplicantRequest.validateFields();
        return mortgageCalculatorService.mortageEligibilityCheck(mortgageApplicantRequest);
    }

}
