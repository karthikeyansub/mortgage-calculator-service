package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.MortgageApplicantRequest;
import com.bank.mortgage.calculator.domain.MortgageApplicantResponse;
import com.bank.mortgage.calculator.domain.MortgageInterestRate;
import com.bank.mortgage.calculator.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MortgageCalculatorServiceTest {

    private MortgageCalculatorService classUnderTest;

    @Mock
    private InterestRateCacheService mockInterestRateCacheService;

    @BeforeEach
    void setUp() {
        classUnderTest = new MortgageCalculatorService(mockInterestRateCacheService);
    }

    @Nested
    @DisplayName("Happy Flow")
    class HappyFlow {

        @Test
        void testMortageEligibilityCheck_ReturnMortgageFeasibilityTrueWithMonthlyCost() {
            when(mockInterestRateCacheService.getInterestRateByMaturityPeriod(10)).thenReturn(
                    new MortgageInterestRate(10, 3.50, LocalDateTime.now()));

            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(50000))
                    .maturityPeriod(10)
                    .loanValue(new BigDecimal(200000))
                    .homeValue(new BigDecimal(225000))
                    .build();
            MortgageApplicantResponse result = classUnderTest.mortageEligibilityCheck(request);

            assertTrue(result.feasible());
            assertNotNull(result.costs());
        }

        @Test
        void testMortageEligibilityCheck_ReturnMortgageFeasibilityFalse_WhenIncomeIsLow() {
            when(mockInterestRateCacheService.getInterestRateByMaturityPeriod(10)).thenReturn(
                    new MortgageInterestRate(10, 3.50, LocalDateTime.now()));

            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(40000))
                    .maturityPeriod(10)
                    .loanValue(new BigDecimal(200000))
                    .homeValue(new BigDecimal(225000))
                    .build();
            MortgageApplicantResponse result = classUnderTest.mortageEligibilityCheck(request);

            assertFalse(result.feasible());
            assertNull(result.costs());
        }

        @Test
        void testMortageEligibilityCheck_ReturnMortgageFeasibilityFalse_WhenLoanValueHigherThenHomeValue() {
            when(mockInterestRateCacheService.getInterestRateByMaturityPeriod(10)).thenReturn(
                    new MortgageInterestRate(10, 3.50, LocalDateTime.now()));

            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(50000))
                    .maturityPeriod(10)
                    .loanValue(new BigDecimal(225000))
                    .homeValue(new BigDecimal(200000))
                    .build();
            MortgageApplicantResponse result = classUnderTest.mortageEligibilityCheck(request);

            assertFalse(result.feasible());
            assertNull(result.costs());
        }
    }

    @Nested
    @DisplayName("Error Flow")
    class ErrorFlow {

        @Test
        void testMortageEligibilityCheck_ThrowInvalidMaturityPeriodException_WhenGivenMaturityPriodIsWrong() {
            when(mockInterestRateCacheService.getInterestRateByMaturityPeriod(10)).thenReturn(
                    new MortgageInterestRate(10, 3.50, LocalDateTime.now()));

            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(50000))
                    .maturityPeriod(30)
                    .loanValue(new BigDecimal(200000))
                    .homeValue(new BigDecimal(225000))
                    .build();
            assertThrows(InvalidInputException.class, () -> classUnderTest.mortageEligibilityCheck(request));
        }
    }
}