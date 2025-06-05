

# Portal de Empleo para Mayores de 50

Este proyecto es una API REST desarrollada en **Java** con **Spring Boot** y **Maven** para gestionar ofertas de empleo dirigidas a personas mayores de 50 años. Permite a las empresas publicar, actualizar y eliminar ofertas, así como a los usuarios consultar y postularse a las mismas.

## Características

- Gestión de empresas y ofertas de empleo.
- CRUD completo de ofertas de trabajo.
- Seguridad con Spring Security.
- Control de vistas y aplicaciones por oferta y empresa.
- Validaciones y manejo de errores.
- Arquitectura basada en DTOs y servicios.

## Tecnologías

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security
- Maven
- Lombok
- Base de datos relacional: MYSQL

## Estructura del Proyecto

- `controllers/`: Controladores REST.
- `dto/`: Objetos de transferencia de datos.
- `entities/`: Entidades JPA.
- `repositories/`: Repositorios de acceso a datos.
- `services/`: Lógica de negocio y servicios.
- `security/`: Seguridad y autenticación.

## Instalación

1. Clona el repositorio:
   ```bash
   https://github.com/PacoMatias89/backend_portal_empleo_mayor50.git
   ```
2. Configura la base de datos en `src/main/resources/application.properties`.
3. Compila el proyecto:
   ```bash
   mvn clean install
   ```
4. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```

## Endpoints principales

- `POST /api/company/job-offers`: Crear oferta de trabajo (requiere autenticación).
- `GET /api/company/job-offers/getAllJobOffer`: Listar todas las ofertas.
- `GET /api/company/job-offers/getJobOfferById/{id}`: Obtener oferta por ID.
- `PUT /api/company/job-offers`: Actualizar oferta (requiere autenticación).
- `DELETE /api/company/job-offers`: Eliminar oferta (requiere autenticación).

## Contribución

1. Haz un fork del repositorio.
2. Crea una rama para tu feature o fix.
3. Haz tus cambios y realiza un commit.
4. Envía un pull request.

## Licencia

Este proyecto está bajo la licencia MIT.

---

Desarrollado por Francisco Molina.
```
