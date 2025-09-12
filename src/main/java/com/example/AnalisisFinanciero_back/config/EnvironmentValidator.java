package com.example.AnalisisFinanciero_back.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentValidator implements InitializingBean {

    @Value("${jwt.secret:}")
    private String jwtSecret;

    @Value("${jwt.expirationMs:0}")
    private int jwtExpiration;

    @Value("${spring.datasource.username:}")
    private String dbUsername;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    @Override
    public void afterPropertiesSet() {
        validateEnvironmentVariables();
    }

    private void validateEnvironmentVariables() {
        StringBuilder errors = new StringBuilder();

        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            errors.append("La variable JWT_SECRET no está configurada en el entorno.\n");
        }

        if (jwtExpiration <= 0) {
            errors.append("La variable JWT_EXPIRATION no está configurada correctamente en el entorno.\n");
        }

        if (dbUsername == null || dbUsername.trim().isEmpty()) {
            errors.append("La variable DB_USERNAME no está configurada en el entorno.\n");
        }

        if (dbPassword == null || dbPassword.trim().isEmpty()) {
            errors.append("La variable DB_PASSWORD no está configurada en el entorno.\n");
        }

        if (errors.length() > 0) {
            throw new IllegalStateException(
                    "Error en la configuración del entorno:\n" + errors.toString() +
                            "Por favor configure las variables de entorno en el archivo .env o en su sistema.");
        }
    }
}
