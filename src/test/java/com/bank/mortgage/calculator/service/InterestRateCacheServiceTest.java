package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.MortgageInterestRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class InterestRateCacheServiceTest {

    private static final String INTEREST_RATE_CACHED_LIST = "interestRateCachedList";

    private static final String INTEREST_RATE_CACHED_MAP = "interestRateCachedMap";

    private InterestRateCacheService classUnderTest;

    @Mock
    private CacheManager mockCacheManager;

    @Mock
    private Cache mockCache;

    @BeforeEach
    void setUp() {
        when(mockCacheManager.getCache("interestRateCacheManager")).thenReturn(mockCache);
        classUnderTest = new InterestRateCacheService(mockCacheManager);
    }

    @Test
    void testGetInterestRates_ShouldGetResultFromDB_WhenNoResultFoundInCache() {
        when(mockCache.get(INTEREST_RATE_CACHED_LIST, List.class)).thenReturn(new ArrayList<>());

        List<MortgageInterestRate> result = classUnderTest.getInterestRates();

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetInterestRates_ShouldGetResultFromCache_WhenResultFoundInCache() {
        when(mockCache.get(INTEREST_RATE_CACHED_LIST, List.class)).thenReturn(
                getMockInterestDataList());
        List<MortgageInterestRate> result = classUnderTest.getInterestRates();

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetInterestRateByMaturityPeriod_ShouldGetResultFromDB_WhenNoResultFoundInCache() {
        when(mockCache.get(INTEREST_RATE_CACHED_MAP, Map.class)).thenReturn(Collections.EMPTY_MAP)
                .thenReturn(getMockInterestDataMap());

        MortgageInterestRate result = classUnderTest.getInterestRateByMaturityPeriod(1);

        assertNotNull(result);
        assertEquals( 2.25d, result.interestRate());
        verify(mockCache, times(2)).get(INTEREST_RATE_CACHED_MAP, Map.class);
    }

    @Test
    void testGetInterestRateByMaturityPeriod_ShouldGetResultFromCache_WhenResultFoundInCache() {
        when(mockCache.get(INTEREST_RATE_CACHED_MAP, Map.class)).thenReturn(
                getMockInterestDataMap());
        MortgageInterestRate result = classUnderTest.getInterestRateByMaturityPeriod(1);

        assertNotNull(result);
        assertEquals( 2.25d, result.interestRate());
    }

    @Test
    void testGetInterestRateByMaturityPeriod_ShouldReturnNull_WhenMaturityPeriodNotPresent() {
        when(mockCache.get(INTEREST_RATE_CACHED_MAP, Map.class)).thenReturn(
                getMockInterestDataMap());
        MortgageInterestRate result = classUnderTest.getInterestRateByMaturityPeriod(11);

        assertNull(result);
    }

    private List<MortgageInterestRate> getMockInterestDataList() {
        List<MortgageInterestRate> data = new ArrayList<>();
        data.add(new MortgageInterestRate(1, 2.25, LocalDateTime.now()));
        return data;
    }

    private Map<Integer, MortgageInterestRate> getMockInterestDataMap() {
        Map<Integer, MortgageInterestRate> data = new HashMap<>();
        data.put(1, new MortgageInterestRate(1, 2.25, LocalDateTime.now()));
        return data;
    }
}