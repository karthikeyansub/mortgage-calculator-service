package com.bank.mortgage.calculator.repository;

import com.bank.mortgage.calculator.domain.Entity.MortgageInterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MortgageInterestRateRepository extends JpaRepository<MortgageInterestRate, Integer> {
}
