package com.bank.mortgage.calculator.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InterestRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Happy Flow")
    class HappyFlow {

        @Test
        void testGetInterestRate() throws Exception {
            mockMvc.perform(get("/api/interest-rates"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is2xxSuccessful());
        }

    }

}