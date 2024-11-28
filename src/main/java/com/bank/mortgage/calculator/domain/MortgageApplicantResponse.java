package com.bank.mortgage.calculator.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public record MortgageApplicantResponse(boolean feasible,
                                        BigDecimal costs) implements Serializable {
}
