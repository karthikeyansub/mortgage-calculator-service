package com.bank.mortgage.calculator.service;

import com.bank.mortgage.calculator.domain.Entity.MortgageInterestRate;
import com.bank.mortgage.calculator.repository.MortgageInterestRateRepository;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class InterestRateCacheService {

    private static final String INTEREST_RATE_CACHE_MANAGER = "interestRateCacheManager";

    private static final String INTEREST_RATE_CACHED_LIST = "interestRateCachedList";

    private static final String INTEREST_RATE_CACHED_MAP = "interestRateCachedMap";

    private final MortgageInterestRateRepository mortgageInterestRateRepository;

    private final Cache cache;

    public InterestRateCacheService(final MortgageInterestRateRepository mortgageInterestRateRepository,
                                    CacheManager cacheManager) {
        this.mortgageInterestRateRepository = mortgageInterestRateRepository;
        this.cache = cacheManager.getCache(INTEREST_RATE_CACHE_MANAGER);
        if(this.cache == null) {
            log.error("Could not retrieve cache (cacheName={}) from the cache manager.", INTEREST_RATE_CACHE_MANAGER);
        }
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
            List<MortgageInterestRate> mortgageInterestRateList = updateInterestRates();
            mortgageInterestRateMap = mortgageInterestRateList.stream().collect(Collectors.toMap(MortgageInterestRate::getMaturityPeriod,
                    Function.identity()));
            this.cache.put(INTEREST_RATE_CACHED_MAP, mortgageInterestRateMap);
        }
        return mortgageInterestRateMap.get(maturityPeriod);
    }

    private List<MortgageInterestRate> updateInterestRates() {
        List<MortgageInterestRate> mortgageInterestRateList = mortgageInterestRateRepository.findAll();
        this.cache.put(INTEREST_RATE_CACHED_LIST, mortgageInterestRateList);

        return mortgageInterestRateList;
    }

}
