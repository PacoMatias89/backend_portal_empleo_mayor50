# 🎯 Portal de Empleo para Mayores de 50

> API REST desarrollada en **Java** con **Spring Boot** para fomentar oportunidades laborales para personas mayores de 50 años.

---

## 🚀 Características

- 🧾 Gestión completa de **empresas** y **ofertas de empleo**
- 🔄 CRUD completo para ofertas de trabajo
- 🔐 Seguridad integrada con **Spring Security**
- 📊 Control de **vistas** y **postulaciones** por oferta y empresa
- ✅ Validaciones sólidas y manejo de errores
- 🧩 Arquitectura limpia basada en **DTOs** y **servicios**

---

## 🛠️ Tecnologías

- ☕ Java 17+
- 🌱 Spring Boot
- 🗃️ Spring Data JPA
- 🛡️ Spring Security
- ⚙️ Maven
- ✨ Lombok
- 💾 MySQL (base de datos relacional)

---

## 🧭 Estructura del Proyecto

```
📦 src/
 ┣ 📂 controllers/      → Controladores REST
 ┣ 📂 dto/              → Data Transfer Objects
 ┣ 📂 entities/         → Entidades JPA
 ┣ 📂 repositories/     → Acceso a datos
 ┣ 📂 services/         → Lógica de negocio
 ┗ 📂 security/         → Seguridad y autenticación
```

---

## ⚙️ Instalación

1. 📥 Clona el repositorio:
   ```bash
   git clone https://github.com/PacoMatias89/backend_portal_empleo_mayor50.git
   ```
2. 🛠️ Configura la base de datos y SSL en `src/main/resources/application.properties`
3. 🧹 Compila el proyecto:
   ```bash
   mvn clean install
   ```
4. ▶️ Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```

---

## 📌 Endpoints principales

| Método | Endpoint                                                  | Descripción                            |
|--------|-----------------------------------------------------------|----------------------------------------|
| POST   | `/api/company/job-offers`                                 | Crear oferta (autenticación requerida) |
| GET    | `/api/company/job-offers/getAllJobOffer`                  | Listar todas las ofertas               |
| GET    | `/api/company/job-offers/getJobOfferById/{id}`            | Obtener oferta por ID                  |
| PUT    | `/api/company/job-offers`                                 | Actualizar oferta (requiere auth)      |
| DELETE | `/api/company/job-offers`                                 | Eliminar oferta (requiere auth)        |

---

## 📄 Licencia

Este proyecto está licenciado bajo la [MIT License](LICENSE).

---

## 👨‍💻 Autor

Desarrollado por **Francisco Molina**.
