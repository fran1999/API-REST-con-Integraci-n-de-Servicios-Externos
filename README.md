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

## ▶️ Instalación rápida

```bash
git clone https://github.com/usuario/proyecto.git
cd proyecto
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

---

## ▶️ Cómo levantar el proyecto

### Opción A — Modo desarrollo (hot reload)
```bash
./mvnw compile quarkus:dev
```

Si tu IDE muestra “Skipping quarkus:dev…”, usar:
```bash
./mvnw compile quarkus:dev -Dquarkus.enforceBuildGoal=false
```

---

## 🌐 URLs importantes
- **API principal** → [http://localhost:8080/posts](http://localhost:8080/posts)
- **Swagger UI** → [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)
- **OpenAPI JSON** → [http://localhost:8080/q/openapi](http://localhost:8080/q/openapi)

---

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas sencilla y organizada:

```
src/main/java/org/acm
├── api/         # Controladores REST (PostResource)
├── service/     # Lógica de negocio (PostService)
├── ext/         # Cliente REST externo (JsonPlaceholderClient)
├── dto/         # Objetos de transferencia de datos (PostDto, CommentDto, UserDto, MergedPostDto)
├── error/       # ExceptionMappers para errores y validaciones
```

**Flujo principal:**
1. Los endpoints REST (api/PostResource) reciben las solicitudes del cliente.
2. La lógica se delega al servicio (service/PostService).
3. Las llamadas externas se realizan a través del REST Client tipado (ext/JsonPlaceholderClient).
4. Los datos externos se transforman a objetos DTO, y luego se unen (merge) en una respuesta enriquecida (MergedPostDto).
5. El cache almacena temporalmente los datos externos para mejorar la performance.
6. Los ExceptionMappers garantizan respuestas JSON consistentes.

---

## 📚 Endpoints disponibles

| Método | Endpoint        | Descripción                             |
|--------|----------------|-----------------------------------------|
| GET    | `/posts`       | Lista posts con autor y comentarios     |
| DELETE | `/posts/{id}`  | Elimina un post remoto en JSONPlaceholder |

### Ejemplos de uso

**GET /posts**
```bash
curl http://localhost:8080/posts | jq
```

Respuesta ejemplo (parcial):
```json
[
  {
    "id": 1,
    "title": "sunt aut facere...",
    "body": "quia et suscipit...",
    "author": {
      "id": 1,
      "name": "Leanne Graham",
      "email": "Sincere@april.biz"
    },
    "comments": [
      {
        "id": 1,
        "email": "Eliseo@gardner.biz",
        "body": "laudantium enim quasi..."
      }
    ]
  }
]
```

**DELETE /posts/{id}**
```bash
curl -i -X DELETE http://localhost:8080/posts/1
# HTTP/1.1 204 No Content

curl -i -X DELETE http://localhost:8080/posts/0
# HTTP/1.1 400 Bad Request
```

---

## 🧪 Tests

Para correr los tests:

```bash
./mvnw test
```


## 📝 Decisiones técnicas tomadas

- **Integración eficiente**: Se consumen 3 endpoints externos (/posts, /comments, /users) y se hace el merge local (evita N+1).
- **Cache**: Se usa `quarkus-cache` (Caffeine) con expiraciones cortas (60–300s).
- **Validaciones de entrada**: Uso de Bean Validation (@Min, @Positive) y sanitización básica de strings.
- **Manejo de errores**:
  - `WebAppExceptionMapper`: propaga status de errores HTTP externos (404, 500).
  - `GenericExceptionMapper`: maneja errores internos con JSON consistente.
- **Timeouts configurados**: `connect-timeout=2000ms`, `read-timeout=4000ms`.
- **Observabilidad**: Logging básico y opción de logging de request/response del Rest Client.

---

