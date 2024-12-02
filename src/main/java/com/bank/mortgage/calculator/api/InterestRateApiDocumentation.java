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
        description = " This API will return a list of current interest rates.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = """
                            Returns a list of current interest rates.
                            NOTE: An empty object will be returned if no records are found.
                        """
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InterestRateApiDocumentation {
}
