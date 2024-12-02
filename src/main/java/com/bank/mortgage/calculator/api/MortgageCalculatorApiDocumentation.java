package com.bank.mortgage.calculator.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Mortgage loan eligibility check.",
        description = """
                     This API will checks the applicant's eligibility for a mortgage loan.
                     If eligible, it also calculates the monthly payment amount and return it in the response.
            """)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = """
                    If customer eligible for mortgage then return feasible true, along with the monthly mortgage cost (amount).
                    otherwise, it returns feasible false, with the message why the mortgage is not feasible.
                    """),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MortgageCalculatorApiDocumentation {
}
