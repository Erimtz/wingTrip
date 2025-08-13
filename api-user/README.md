# API User - WingTrip Microservice



⚠ **Nota:** Este microservicio actualmente NO incluye autenticación/autorización. Se agregará Keycloak en futuras versiones para gestión completa de seguridad.



## 📋 Descripción



Microservicio de gestión de usuarios para la plataforma WingTrip. Maneja operaciones CRUD básicas de usuarios y registro en Eureka para service discovery. Forma parte de una arquitectura de microservicios más amplia que incluirá sistema de reservas, pagos y gestión de vuelos.



## 🏗 Arquitectura



- **Framework:** Spring Boot 3.x

- **Base de Datos:** MySQL 8.0

- **Puerto:** 8080 (interno), 8081 (externo via Docker)

- **Service Discovery:** Eureka Client

- **API Gateway:** Spring Cloud Gateway

- **Seguridad:** Sin implementar (Keycloak pendiente)



## 🗄 Modelo de Datos



### Entidad User



```sql

-- Base de datos: user_db

CREATE TABLE user (

     user_id BIGINT AUTO_INCREMENT PRIMARY KEY,

     name VARCHAR(255) NOT NULL,

     lastname VARCHAR(255) NOT NULL,

     address VARCHAR(255),

     email VARCHAR(255) NOT NULL,

     username VARCHAR(255) NOT NULL,

     password VARCHAR(255) NOT NULL -- Se eliminará con Keycloak

);

```



## 🚀 Instalación y Configuración



### Prerrequisitos



- Java 17+

- Maven 3.8+

- Docker & Docker Compose

- MySQL 8.0 (via Docker)



### Variables de Entorno (.env en raíz del proyecto)



⚠ **Nota de Seguridad:** Para proyectos de producción, NUNCA expongas credenciales reales. Este es un proyecto de práctica con credenciales de ejemplo.



```properties

# MySQL Configuration

DATABASE_ROOT_PASSWORD=rootpassword123

DATABASE_USERNAME=wingtrip_user

DATABASE_PASSWORD=1324



# RabbitMQ Configuration (usado por otros microservicios)

RABBITMQ_USERNAME=admin

RABBITMQ_PASSWORD=admin123

```



### Instalación con Docker Compose (Recomendado)



#### 1. Monorepo completo



```bash

# Clonar el monorepo completo

git clone <wingtrip-repository-url>

cd wingtrip



# Levantar toda la infraestructura

docker-compose up --build



# O solo las dependencias de api-user

docker-compose up mysql-user config-server eureka-server

```



#### 2. Solo api-user para desarrollo



```bash

cd api-user



# Construir aplicación

mvn clean install



# Ejecutar local (requiere servicios externos)

mvn spring-boot:run -Dspring.profiles.active=local

```



### Orden de arranque de servicios



1. **mysql-user** (puerto 3310)

2. **config-server** (puerto 8888)

3. **eureka-server** (puerto 8761)

4. **api-user** (puerto 8081)

5. **api-gateway** (puerto 8080)



## 📚 API Endpoints



### Acceso via API Gateway (Recomendado)



```http

# Base URL: http://localhost:8081/api/v1/user



# Gestión de Usuarios

GET    /api/v1/user/get-all                      # Listar todos los usuarios

POST   /api/v1/user/create                       # Crear nuevo usuario

GET    /api/v1/user/find/{id}                    # Buscar usuario por ID

GET    /api/v1/user/profile/{username}           # Buscar usuario por username

PUT    /api/v1/user/update/id/{id}               # Actualizar usuario por ID

PUT    /api/v1/user/update/username/{username}   # Actualizar usuario por username

DELETE /api/v1/user/delete/{id}                  # Eliminar usuario por ID



# Validaciones y Verificaciones

GET /api/v1/user/validateEmail/{email}           # Validar formato email

GET /api/v1/user/validateUsername/{username}     # Validar formato username

GET /api/v1/user/existsByEmail/{email}           # Verificar si email existe

GET /api/v1/user/existsByUsername/{username}     # Verificar si username existe

```



### Acceso directo (desarrollo)



```http

# Base URL: http://localhost:8081/api/v1/user

# Nota: Docker expone el puerto interno 8080 como 8081 externamente

```



### Ejemplos de Uso



#### Crear Usuario



```bash

curl -X POST http://localhost:8081/api/v1/user/create \

   -H "Content-Type: application/json" \

   -d '{

     "name": "Juan",

     "lastname": "Pérez", 

     "address": "Calle 123 #45-67",

     "email": "juan.perez@email.com",

     "username": "jperez",

     "password": "password123"

   }'

```



#### Buscar Usuario



```bash

# Por ID

curl http://localhost:8081/api/v1/user/find/1



# Por username

curl http://localhost:8081/api/v1/user/profile/jperez

```



#### Validar disponibilidad



```bash
# Verificar si email existe
curl http://localhost:8081/api/v1/user/existsByEmail/juan.perez@email.com

# Verificar si username existe
curl http://localhost:8081/api/v1/user/existsByUsername/jperez
```



⚠ **Importante:** Todos los endpoints están sin autenticación - Solo para desarrollo



## 📖 Documentación API



- **Swagger UI:** http://localhost:8081/swagger-ui/index.html#/

- **Health Check:** http://localhost:8081/actuator/health

- **OpenAPI JSON:** http://localhost:8081/v3/api-docs



## 🧪 Testing



### Ejecutar Tests



```bash

# Todos los tests

mvn test



# Solo tests unitarios

mvn test -Dtest="\*UnitTest"

```



### Coverage



Los tests cubren la funcionalidad principal del microservicio con JUnit 5 y Mockito.



### Postman Collection



Para exportar y usar la colección de Postman:



1. **Exportar desde Postman:**

    - Abre Postman

    - Selecciona tu colección "WingTrip API User"

    - Click en "..." → Export

    - Guarda como `WingTrip-API-User.postman_collection.json`

    - Colócalo en la carpeta `/postman/` del proyecto



2. **Importar en otro Postman:**

    - File → Import → Seleccionar el archivo JSON exportado



## 🐳 Configuración Docker



### Dockerfile



```dockerfile

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/\*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

```



### Docker Compose Integration



- **Network:** wingtrip-network  

- **Database:** mysql-user:3310

- **Dependencies:** config-server, eureka-server

- **Health Checks:** Configurados para todos los servicios



## 🔧 Tecnologías y Dependencias



| Tecnología | Propósito | Versión |

|------------|-----------|---------|

| Spring Boot | Framework principal | 3.x |

| Spring Cloud | Microservicios | 2022.x |  

| Spring Data JPA | Persistencia | - |

| MySQL Connector | Driver BD | - |

| Eureka Client | Service Discovery | - |

| MapStruct | Mapping DTOs | 1.5.3 |

| Lombok | Boilerplate reduction | 1.18.30 |

| SpringDoc OpenAPI | Documentación API | 2.6.0 |

| JUnit 5 | Testing unitario | - |

| Mockito | Mocking en tests | - |

| Easy Random | Test data generation | 5.0.0 |



## 🌐 Integración con Ecosystem



### Service Discovery (Eureka)



```yaml

eureka:

   client:

     registerWithEureka: true

     fetchRegistry: true

     service-url:

       defaultZone: http://eureka-server:8761/eureka/

```



### API Gateway Routes



```yaml

spring:

   cloud:

     gateway:

       routes:

         - id: ms-user

           uri: lb://api-user  # Load balanced

           predicates:

             - Path=/api/v1/user/**

```



## 📊 Monitorización



### Health Checks



- **Actuator:** http://localhost:8081/actuator/health

- **Database:** Verificación automática de conexión

- **Eureka:** Estado de registro



### Métricas disponibles



- http://localhost:8081/actuator/metrics

- http://localhost:8081/actuator/info



## 🔄 Roadmap



### Próximas Versiones



#### v2.0.0 - Seguridad con Keycloak



- 🔄 Implementar autenticación con Keycloak

- 🔄 Agregar autorización por roles  

- 🔄 Remover campo password

- 🔄 Configurar integración con API Gateway

- 🔄 Proteger endpoints con tokens de Keycloak



## 🚨 Consideraciones Importantes



### Estado Actual - Desarrollo



- 🚀 **CRUD completo** implementado y testeado

- 🧪 **Tests unitarios** con JUnit + Mockito  

- 📖 **Swagger documentación** disponible

- 🔍 **Service Discovery** integrado

- ✅ **Response DTOs** optimizados implementados

- 🚫 **Sin autenticación** - Endpoints abiertos

- ⚠ **Solo para desarrollo** - No usar en producción



### Arquitectura Microservicios



- 🌐 **API Gateway** como punto de entrada único

- 📡 **Eureka** para service discovery  

- 🐳 **Docker** para containerización

- 🔗 **Load Balancing** automático via Spring Cloud Gateway

- 🔧 **Configuración centralizada** via Config Server



### Base de Datos



- 📊 **MySQL 8.0** en contenedor Docker

- 🔗 **Connection pooling** configurado

- 📝 **Schema/Data initialization** automática

- 🏥 **Health checks** implementados



### Servicios Relacionados



- **Zipkin** (puerto 9411): Usado por api-payment para trazabilidad y pruebas de estrés

- **RabbitMQ** (puerto 5672/15672): Cola de mensajería entre api-payment y api-booking



## 📞 Contacto



- **Proyecto:** WingTrip Platform

- **Desarrollador:** Erika Martínez

- **Email:** erimtz2@gmail.com

- **Repositorio:** [Monorepo wingTrip] (https://github.com/Erimtz/wingTrip)



## 📄 Licencia



Este proyecto es parte de WingTrip Platform - Todos los derechos reservados.



## 🔗 Enlaces Útiles



- **Eureka Dashboard:** http://localhost:8761

- **API Gateway Health:** http://localhost:8080/actuator/health  

- **API User Health:** http://localhost:8081/actuator/health

- **API User Swagger:** http://localhost:8081/swagger-ui/index.html#/

- **MySQL:** localhost:3310 (user_db)



---



📝 **Nota para desarrolladores:** Este README será actualizado cuando se complete la migración a Keycloak. La funcionalidad actual es completamente funcional para desarrollo y testing dentro del ecosistema WingTrip.

