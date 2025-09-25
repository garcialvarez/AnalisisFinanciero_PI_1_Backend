package com.udea.AnalisisFinanciero_back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${ALLOWED_ORIGINS:}")
    private String allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Configurar CORS para permitir solo orígenes específicos si están configurados
                // o permitir todos si no hay ninguno configurado
                String[] origins;

                if (StringUtils.hasText(allowedOrigins)) {
                    origins = allowedOrigins.split(",");
                } else {
                    origins = new String[] {"*"}; // Permite todos los orígenes si no hay lista configurada
                }

                registry.addMapping("/**")
                        .allowedOrigins(origins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .maxAge(3600); // Tiempo de caché para las respuestas pre-flight
            }
        };
    }
}