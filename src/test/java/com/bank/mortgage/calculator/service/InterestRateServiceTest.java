package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.Entity.MortgageInterestRate;
import com.bank.mortgage.calculator.domain.MortgageInterestRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class InterestRateServiceTest {

    private InterestRateService classUnderTest;

    @Mock
    private InterestRateCacheService mockinterestRateCacheService;

    @BeforeEach
    void setUp() {
        classUnderTest = new InterestRateService(mockinterestRateCacheService);
    }

    @Test
    void testGetInterestRates_ShouldReturnListOfInterestRateResponse() {
        List<MortgageInterestRate> data = new ArrayList<>();
        data.add(new MortgageInterestRate(1, 2.25, LocalDateTime.now()));
        when(mockinterestRateCacheService.getInterestRates()).thenReturn(data);

        List<MortgageInterestRateResponse> result = classUnderTest.getInterestRates();

        assertFalse(result.isEmpty());
        verify(mockinterestRateCacheService, times(1)).getInterestRates();
    }

    @Test
    void testGetInterestRates_ShouldReturnEmptyResponse_WhenCacheServiceReturnsNull() {
        when(mockinterestRateCacheService.getInterestRates()).thenReturn(null);

        List<MortgageInterestRateResponse> result = classUnderTest.getInterestRates();

        assertTrue(result.isEmpty());
        verify(mockinterestRateCacheService, times(1)).getInterestRates();
    }

    @Test
    void testGetInterestRates_ShouldReturnEmptyResponse_WhenCacheServiceReturnsEmpty() {
        when(mockinterestRateCacheService.getInterestRates()).thenReturn(new ArrayList<>());

        List<MortgageInterestRateResponse> result = classUnderTest.getInterestRates();

        assertTrue(result.isEmpty());
        verify(mockinterestRateCacheService, times(1)).getInterestRates();
    }
}