package com.bank.mortgage.calculator.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Retrieve list of current interest rates.",
        description = " This api will return list of current interest rates based on the maturity period.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = """
                            Retrieves list of current interest rates with below fields.
                            The mortgage rate object contains the fields:
                                - maturityPeriod (integer)
                                - interestRate (Percentage)
                                - lastUpdate (Timestamp)
                            NOTE: It will return empty object if no records found.
                        """
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InterestRateApiDocumentation {
}
