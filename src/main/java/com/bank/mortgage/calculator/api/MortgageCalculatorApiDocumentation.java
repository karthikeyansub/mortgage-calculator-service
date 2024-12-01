package com.bank.mortgage.calculator.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "To calculate for a mortgage loan eligibility.",
        description = """
                     This API will check whether the applicant is eligible to get mortgage loan.
                     If yes, it calculates the monthly payment amount as well and return in the response.
            """)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "It returns whether the mortgage is feasible (boolean) and the montly costs (Amount) of the mortgage."),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MortgageCalculatorApiDocumentation {
}
