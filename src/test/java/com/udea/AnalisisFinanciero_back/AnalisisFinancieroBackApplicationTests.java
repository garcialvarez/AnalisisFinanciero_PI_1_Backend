package com.udea.AnalisisFinanciero_back;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class AnalisisFinancieroBackApplicationTests {

	@Test
	void contextLoads() {
		// Esta prueba verifica que el contexto de Spring se cargue correctamente
	}

}