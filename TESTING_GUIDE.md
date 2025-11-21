# Guía de comandos para pruebas y análisis

## 1. Ejecutar todas las pruebas
./mvnw clean test

## 2. Generar reporte de cobertura con JaCoCo
./mvnw clean test jacoco:report

## 3. Ver reporte de cobertura (abrir en navegador)
# Archivo: target/site/jacoco/index.html

## 4. Ejecutar análisis completo (requiere configuración SonarCloud)
./mvnw clean verify sonar:sonar \
  -Dsonar.projectKey=TU_PROJECT_KEY \
  -Dsonar.organization=TU_ORGANIZATION \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=TU_SONAR_TOKEN

## 5. Ejecutar solo compilación sin pruebas
./mvnw clean compile

## 6. Ejecutar pruebas de una clase específica
./mvnw test -Dtest=AuthServiceTest

## 7. Ejecutar pruebas con perfil específico
./mvnw test -Dspring.profiles.active=test

## Variables de entorno requeridas para desarrollo local:
# JWT_SECRET=mySecretKey1234567890mySecretKey1234567890mySecretKey1234567890
# DB_URL=jdbc:postgresql://localhost:5432/analisis_financiero
# DB_USERNAME=tu_usuario
# DB_PASSWORD=tu_password