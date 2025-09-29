# Prueba Técnica – Quarkus REST + Integración externa

API REST en **Quarkus 3.x (Java 17)** que integra datos de **JSONPlaceholder** y expone endpoints propios con **validación de entradas**, **sanitización**, **manejo de errores estandarizado**, **timeouts**, **cache** y **tests**.

---

## 🚀 Stack
- **Quarkus 3.x** (`quarkus-rest-jackson`)
- **REST Client** (`quarkus-rest-client-jackson`) para JSONPlaceholder
- **OpenAPI/Swagger UI** (`quarkus-smallrye-openapi`)
- **Bean Validation** (`quarkus-hibernate-validator`)
- **Cache** en memoria (`quarkus-cache`)
- **Testing**: `quarkus-junit5` + `rest-assured`
- *(Opcional PRO)* **Fault Tolerance** (`quarkus-smallrye-fault-tolerance`) para `@Timeout`/`@Retry`

---

## ⚙️ Requisitos
- **Java 17** (OpenJDK)
- **Maven 3.8+**
- Acceso a Internet (para las llamadas a JSONPlaceholder)

---

## ▶️ Cómo levantar el proyecto

### Opción A — Modo desarrollo (hot reload)
```bash
./mvnw compile quarkus:dev
```
```bash
# Si tu IDE muestra “Skipping quarkus:dev…”, usar:
./mvnw compile quarkus:dev -Dquarkus.enforceBuildGoal=false
```

Aplicación 
http://localhost:8080/posts


Swagger UI (documentación interactiva)
http://localhost:8080/q/swagger-ui

Esquema OpenAPI (JSON)
http://localhost:8080/q/openapi

