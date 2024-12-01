package com.bank.mortgage.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MortgageCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MortgageCalculatorApplication.class, args);
	}

}
