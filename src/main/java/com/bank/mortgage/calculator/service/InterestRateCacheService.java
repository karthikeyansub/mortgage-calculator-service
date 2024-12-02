package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.MortgageInterestRate;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class InterestRateCacheService implements SmartInitializingSingleton {

    private static final String INTEREST_RATE_CACHE_MANAGER = "interestRateCacheManager";

    private static final String INTEREST_RATE_CACHED_LIST = "interestRateCachedList";

    private static final String INTEREST_RATE_CACHED_MAP = "interestRateCachedMap";


    private final Cache cache;

    public InterestRateCacheService(final CacheManager cacheManager) {
        this.cache = cacheManager.getCache(INTEREST_RATE_CACHE_MANAGER);
        if(this.cache == null) {
            log.error("Could not retrieve cache (cacheName={}) from the cache manager.", INTEREST_RATE_CACHE_MANAGER);
        }
    }

    /**
     * This method is called on startup of Spring Boot and is blocking the startup until the cache has been loaded.
     */
    @Override
    public void afterSingletonsInstantiated() {
        updateInterestRates();
    }

    public List<MortgageInterestRate> getInterestRates() {
        List<MortgageInterestRate> mortgageInterestRateList = this.cache.get(INTEREST_RATE_CACHED_LIST, List.class);
        if(isNull(mortgageInterestRateList) || mortgageInterestRateList.isEmpty()) {
            log.debug("Cache is empty, updating the interest rates cache list");
            return updateInterestRates();
        }
        return mortgageInterestRateList;
    }

    @Nullable
    public MortgageInterestRate getInterestRateByMaturityPeriod(final Integer maturityPeriod) {
        Map<Integer, MortgageInterestRate> mortgageInterestRateMap = this.cache.get(INTEREST_RATE_CACHED_MAP, Map.class);
        if(isNull(mortgageInterestRateMap) || mortgageInterestRateMap.isEmpty()) {
            log.debug("Cache is empty, updating the interest rates cache map");
            updateInterestRates();
            mortgageInterestRateMap = this.cache.get(INTEREST_RATE_CACHED_MAP, Map.class);
        }
        return mortgageInterestRateMap.get(maturityPeriod);
    }

    private List<MortgageInterestRate> updateInterestRates() {
        List<MortgageInterestRate> mortgageInterestRateList = getInterestRatesData();
        this.cache.put(INTEREST_RATE_CACHED_LIST, mortgageInterestRateList);

        //Update cache in the map for mortgage check to retrieve interest rate for the particular maturity period.
        Map<Integer, MortgageInterestRate>  mortgageInterestRateMap = mortgageInterestRateList.stream().collect(Collectors.toMap(MortgageInterestRate::maturityPeriod,
                Function.identity()));
        this.cache.put(INTEREST_RATE_CACHED_MAP, mortgageInterestRateMap);

        return mortgageInterestRateList;
    }
    
    private static List<MortgageInterestRate> getInterestRatesData() {

        return List.of(new MortgageInterestRate(1, 2.50, LocalDateTime.now()),
                new MortgageInterestRate(2, 2.75, LocalDateTime.now()),
                new MortgageInterestRate(3, 3.00, LocalDateTime.now()),
                new MortgageInterestRate(4, 3.25, LocalDateTime.now()),
                new MortgageInterestRate(5, 3.50, LocalDateTime.now()),
                new MortgageInterestRate(6, 3.75, LocalDateTime.now()),
                new MortgageInterestRate(7, 4.00, LocalDateTime.now()),
                new MortgageInterestRate(8, 4.25, LocalDateTime.now()),
                new MortgageInterestRate(9, 4.50, LocalDateTime.now()),
                new MortgageInterestRate(10, 4.75, LocalDateTime.now())) ;
    }

}
