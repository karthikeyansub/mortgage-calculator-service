package com.bank.mortgage.calculator.api;

import com.bank.mortgage.calculator.domain.ErrorResponse;
import com.bank.mortgage.calculator.domain.MortgageApplicantRequest;
import com.bank.mortgage.calculator.domain.MortgageApplicantResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MortgageCalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("Happy Flow")
    class HappyFlow {

        @Test
        @DisplayName("Applicant get response with mortgage check feasibility true with monthly costs.")
        void testMortgageCalculator_WithFeasibilityTrue() throws Exception {
            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(50000))
                    .maturityPeriod(5)
                    .homeValue(new BigDecimal(225000))
                    .loanValue(new BigDecimal(200000)).build();

            MvcResult result =mockMvc.perform(post("/api/mortgage-check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            MortgageApplicantResponse response = objectMapper.readValue(responseJson, MortgageApplicantResponse.class);
            assertTrue(response.feasible());
            assertNotNull(response.costs());
        }

        @Test
        @DisplayName("Applicant get response with mortgage check feasibility false due to income value is less than the loan value.")
        void testMortgageCalculator_WithFeasibilityFalse_DueToInsufficientIncome() throws Exception {
            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(40000))
                    .maturityPeriod(5)
                    .homeValue(new BigDecimal(225000))
                    .loanValue(new BigDecimal(200000)).build();

            MvcResult result = mockMvc.perform(post("/api/mortgage-check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            MortgageApplicantResponse response = objectMapper.readValue(responseJson, MortgageApplicantResponse.class);
            assertFalse(response.feasible());
            assertNull(response.costs());
        }

        @Test
        @DisplayName("Applicant get response with mortgage check feasibility false due to loan value is greater than the home value.")
        void testMortgageCalculator_WithFeasibilityFalse_DueToLoanValueGreaterThanHomeValue() throws Exception {
            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(50000))
                    .maturityPeriod(5)
                    .homeValue(new BigDecimal(200000))
                    .loanValue(new BigDecimal(225000)).build();

            MvcResult result = mockMvc.perform(post("/api/mortgage-check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            MortgageApplicantResponse response = objectMapper.readValue(responseJson, MortgageApplicantResponse.class);
            assertFalse(response.feasible());
            assertNull(response.costs());
        }

    }

    @Nested
    @DisplayName("Error Flow")
    class ErrorFlow {

        @Test
        @DisplayName("Applicant get error response when income value is null.")
        void testMortgageCalculator_ReturnBadRequest_WhenIncome_IsNull() throws Exception {
            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(null)
                    .maturityPeriod(11)
                    .homeValue(new BigDecimal(225000))
                    .loanValue(new BigDecimal(200000)).build();

            MvcResult result = mockMvc.perform(post("/api/mortgage-check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ErrorResponse response = objectMapper.readValue(responseJson, ErrorResponse.class);
            assertEquals("Income value cannot be null", response.errorMessage());
        }

        @Test
        @DisplayName("Applicant get error response when maturity period is null.")
        void testMortgageCalculator_ReturnBadRequest_WhenMaturityPeriod_IsNull() throws Exception {
            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(50000))
                    .maturityPeriod(null)
                    .homeValue(new BigDecimal(225000))
                    .loanValue(new BigDecimal(200000)).build();

            MvcResult result = mockMvc.perform(post("/api/mortgage-check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ErrorResponse response = objectMapper.readValue(responseJson, ErrorResponse.class);
            assertEquals("Maturity period value cannot be null", response.errorMessage());
        }

        @Test
        @DisplayName("Applicant get error response when home value is null.")
        void testMortgageCalculator_ReturnBadRequest_WhenHome_IsNull() throws Exception {
            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(50000))
                    .maturityPeriod(11)
                    .homeValue(null)
                    .loanValue(new BigDecimal(200000)).build();

            MvcResult result = mockMvc.perform(post("/api/mortgage-check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ErrorResponse response = objectMapper.readValue(responseJson, ErrorResponse.class);
            assertEquals("Home value cannot be null", response.errorMessage());
        }

        @Test
        @DisplayName("Applicant get error response when loan value is null.")
        void testMortgageCalculator_ReturnBadRequest_WhenLoan_IsNull() throws Exception {
            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(50000))
                    .maturityPeriod(11)
                    .homeValue(new BigDecimal(225000))
                    .loanValue(null).build();

            MvcResult result = mockMvc.perform(post("/api/mortgage-check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ErrorResponse response = objectMapper.readValue(responseJson, ErrorResponse.class);
            assertEquals("Loan value cannot be null", response.errorMessage());
        }

        @Test
        @DisplayName("Applicant get error response when no interest rate found for the given maturity period.")
        void testMortgageCalculator_ReturnBadRequest_WhenNoInterestRate_ForTheGivenMaturityPeriod() throws Exception {
            MortgageApplicantRequest request = MortgageApplicantRequest.builder()
                    .income(new BigDecimal(50000))
                    .maturityPeriod(11)
                    .homeValue(new BigDecimal(225000))
                    .loanValue(new BigDecimal(200000)).build();

            MvcResult result = mockMvc.perform(post("/api/mortgage-check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ErrorResponse response = objectMapper.readValue(responseJson, ErrorResponse.class);
            assertEquals("No interest rate found for the given maturity period(11).", response.errorMessage());
        }
    }

    private String covertToJsonString(final MortgageApplicantRequest request) throws JsonProcessingException {
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}