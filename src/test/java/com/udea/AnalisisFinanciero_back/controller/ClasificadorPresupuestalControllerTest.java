package com.udea.AnalisisFinanciero_back.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
@DisplayName("ClasificadorPresupuestalController Integration Tests")
class ClasificadorPresupuestalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return unauthorized when accessing without authentication")
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/clasificador-presupuestal"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return bad request for invalid codigo format")
    void testInvalidCodigoFormat() throws Exception {
        mockMvc.perform(get("/api/clasificador-presupuestal/codigo/"))
                .andExpect(status().isNotFound());
    }
}