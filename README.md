
<div align="center">
  <img src="https://img.shields.io/badge/Java-17-blue?logo=java" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/PostgreSQL-Database-blue?logo=postgresql" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Swagger-API%20Docs-yellow?logo=swagger" alt="Swagger"/>
  <img src="https://img.shields.io/badge/JWT-Security-orange?logo=jsonwebtokens" alt="JWT"/>
</div>

# AnalisisFinanciero-back

Sistema web para la recolección, validación y consolidación automatizada de información presupuestal en la Universidad de Antioquia.

---

## Tabla de Contenidos
- [Resumen](#resumen)
- [Prerrequisitos del Sistema](#prerrequisitos-del-sistema)
- [Instalación y Configuración](#instalación-y-configuración)
- [Configuración de Variables de Entorno](#configuración-de-variables-de-entorno)
- [Comandos de Ejecución](#comandos-de-ejecución)
- [Despliegue](#despliegue)
- [Características del Proyecto](#características-del-proyecto)
- [Endpoints Disponibles](#endpoints-disponibles)
- [Solución de Problemas](#solución-de-problemas)

---

## Resumen
La Vicerrectoría Financiera de la Universidad de Antioquia enfrenta limitaciones en la consolidación de presupuestos institucionales, tradicionalmente gestionados en Excel. Esto genera inconsistencias, reprocesos y pérdida de tiempo. La propuesta es un sistema web que permita a las dependencias registrar necesidades e ingresos en formularios digitales validados, consolidando la información automáticamente y asegurando la integridad de los datos.

**Palabras clave:** Presupuesto digital, validación de datos, sistema web financiero, integridad de la información, automatización, planeación institucional

## Prerrequisitos del Sistema
- Java 17 o superior
- PostgreSQL 13 o superior
- Git

## Instalación y Configuración
1. Clona el repositorio:
   ```bash
   git clone https://github.com/garcialvarez/AnalisisFinanciero_PI_1_Backend.git
   cd AnalisisFinanciero_PI_1_Backend
   ```
2. Instala las dependencias con Maven (el wrapper `./mvnw` ya está incluido).
3. Configura la base de datos PostgreSQL y crea la base de datos indicada en `src/main/resources/application.properties`.

## Configuración de Variables de Entorno
Edita el archivo `src/main/resources/application.properties` para definir:
- `spring.datasource.url` (URL de conexión a la base de datos)
- `spring.datasource.username` (usuario de la base de datos)
- `spring.datasource.password` (contraseña de la base de datos)

## Comandos de Ejecución
Para compilar y ejecutar el backend:
```bash
./mvnw spring-boot:run
```
Accede a la documentación Swagger en: [http://localhost:8080/doc/swagger-ui.html](http://localhost:8080/doc/swagger-ui.html)

## Despliegue
El proyecto puede desplegarse localmente o en cualquier servidor compatible con Java 17 y PostgreSQL. Para despliegue en producción, asegúrate de:
- Configurar correctamente las variables de entorno y la base de datos.
- Usar HTTPS y configurar los perfiles de seguridad.

## Características del Proyecto
- Arquitectura API RESTful con Spring Boot 3
- Seguridad con Spring Security y JWT (incluye autenticación 2FA)
- Documentación automática con Swagger/OpenAPI
- Validación de datos y reglas de negocio robustas
- Consolidación y reportes automáticos
- Control de roles y permisos diferenciados
- Estructura modular: controladores, servicios, repositorios, entidades, DTOs, configuración y seguridad

## Endpoints Disponibles
La documentación de los endpoints está disponible y se irá alimentando a futuro en Swagger:
- [http://localhost:8080/doc/swagger-ui.html](http://localhost:8080/doc/swagger-ui.html)

## Solución de Problemas
- **Error de versión Java:** Asegúrate de tener Java 17 instalado y configurado como JAVA_HOME.
- **Problemas de conexión a la base de datos:** Verifica los datos en `application.properties` y que el servicio de PostgreSQL esté activo.
- **Compilación fallida:** Ejecuta `./mvnw clean install` para limpiar y reconstruir el proyecto.
- **Swagger no carga:** Verifica que la app esté corriendo y accede a la ruta correcta.

---

## Información Adicional

### Estructura del Proyecto
- `src/main/java/com/udea/AnalisisFinanciero_back/`
  - `controller/` Controladores REST
  - `entity/` Entidades JPA
  - `repository/` Repositorios Spring Data
  - `service/` Lógica de negocio
  - `security/` Seguridad y JWT
  - `exceptions/` Manejo de errores global
  - `DTO/` Objetos de transferencia de datos
  - `config/` Configuración de CORS, seguridad y Swagger

### Reglas de Negocio y Validaciones
- Solo usuarios con correo institucional pueden autenticarse.
- Autenticación con 2FA (código enviado y validado).
- Validación de estados de usuario (activo, suspendido, etc.).
- Validación de datos en formularios (tipos, rangos, reglas de negocio).
- Control de roles y permisos diferenciados.
- Consolidación automática y generación de reportes exportables.

### Contribución
1. Realiza fork y crea una rama para tu funcionalidad.
2. Haz pull request describiendo los cambios.

### Licencia

Este proyecto está licenciado bajo los términos de la licencia MIT. Consulta el archivo [LICENSE](./LICENSE) para más detalles.

----

> Para detalles de reglas de negocio, historias de usuario y arquitectura, consulta la documentación y los comentarios en el código fuente.
