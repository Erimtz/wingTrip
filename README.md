# WingTrip - Microservices Platform



🚀 **Plataforma de reservas de vuelos desarrollada con arquitectura de microservicios**



> Estado actual: **En desarrollo** - Proyecto de práctica y aprendizaje



## 📋 Descripción



WingTrip es una plataforma de reservas de vuelos construida con Spring Boot y arquitectura de microservicios. El proyecto implementa patrones modernos como Service Discovery, API Gateway, y está diseñado para escalar horizontalmente.



## 🏗️ Arquitectura General



![Microservices Architecture](docs/microservices-architecture.png)



### Stack Tecnológico



- **Framework:** Spring Boot 3.x + Spring Cloud

- **Base de Datos:** MySQL 8.0

- **Service Discovery:** Netflix Eureka

- **API Gateway:** Spring Cloud Gateway

- **Containerización:** Docker + Docker Compose

- **Trazabilidad:** Zipkin (api-payment)

- **Mensajería:** RabbitMQ (api-payment ↔ api-booking)



### Comunicación entre Microservicios



- **Síncrona:** API REST + Feign Client

- **Asíncrona:** RabbitMQ (eventos de pago y reservas)



## 🗄️ Modelo de Datos



![Database DER](docs/wingtrip-der.png)



\*Nota: El DER será actualizado en v2.0 con la implementación de Keycloak (password → phoneNumber)\*



## 📦 Microservicios



### 🚀 Estado Actual (Fase 1)



| Servicio | Puerto | Estado | Documentación | Descripción |

|----------|---------|--------|---------------|-------------|

| **config-server** | 8888 | ✅ Funcionando | - | Configuración centralizada |

| **eureka-server** | 8761 | ✅ Funcionando | - | Service Discovery |

| **api-gateway** | 8080 | ✅ Funcionando | - | Punto de entrada único |

| **api-user** | 8081 | ✅ Funcionando | [README](api-user/README.md) | Gestión de usuarios |

| **api-booking** | 8082 | 🔄 En desarrollo | - | Gestión de reservas |



### 🔮 Arquitectura Completa Planeada (Futuro)



| Servicio | Puerto | Base de Datos | Tecnologías | Estado |

|----------|---------|---------------|-------------|--------|

| **api-payment** | 8083 | MySQL | Resilience4j + Zipkin + RabbitMQ | 📋 Planeado |

| **api-flight** | 8084 | MongoDB | - | 📋 Planeado |

| **api-flight-details** | 8085 | MongoDB | - | 📋 Planeado |

| **api-seat** | 8086 | MongoDB | Feign Server | 📋 Planeado |



### Infraestructura Actual



| Servicio | Puerto | Uso | Estado |

|----------|---------|-----|---------|

| **MySQL User** | 3310 | Base de datos usuarios | ✅ |

| **MySQL Booking** | 3311 | Base de datos reservas | ✅ |

| **RabbitMQ** | 5672/15672 | Cola mensajería para api-payment | ✅ |

| **Zipkin** | 9411 | Trazabilidad para pruebas de carga | ✅ |



### Infraestructura Futura Planeada



| Servicio | Puerto | Uso | Estado |

|----------|---------|-----|---------|

| **MySQL Payment** | 3312 | Base de datos pagos | 📋 Planeado |

| **MongoDB Flight** | 27017 | Catálogo vuelos | 📋 Planeado |

| **MongoDB Seats** | 27018 | Gestión asientos | 📋 Planeado |

| **MongoDB Details** | 27019 | Detalles vuelos | 📋 Planeado |

| **Keycloak** | 8180 | Autenticación | 📋 Planeado |



## 🚀 Quick Start



### Prerrequisitos



- Docker & Docker Compose

- Java 17+ (para desarrollo local)

- Maven 3.8+ (para desarrollo local)



### Instalación



```bash

# 1. Clonar repositorio

git clone <tu-repo-url>

cd wingtrip



# 2. Configurar variables de entorno

cp .env.example .env

# Editar .env con tus configuraciones



# 3. Levantar toda la plataforma

docker-compose up --build



# 4. Verificar servicios

curl http://localhost:8761  # Eureka Dashboard

curl http://localhost:8080/actuator/health  # Gateway Health

```



### Orden de arranque



Los servicios se levantan automáticamente en este orden:



1. **Bases de datos** (mysql-user, mysql-booking)

2. **Infraestructura** (config-server, eureka-server)

3. **Servicios externos** (zipkin, rabbitmq)  

4. **Microservicios** (api-user, api-booking)

5. **API Gateway**



## 🔧 Desarrollo



### Variables de Entorno



```bash

# Crear archivo .env en la raíz

DATABASE_ROOT_PASSWORD=rootpassword123

DATABASE_USERNAME=wingtrip_user

DATABASE_PASSWORD=1324

RABBITMQ_USERNAME=admin

RABBITMQ_PASSWORD=admin123

```



### Ejecutar microservicio individual



```bash

# Ejemplo con api-user

cd api-user

mvn clean install

mvn spring-boot:run -Dspring.profiles.active=local

```



### Testing



```bash

# Tests de todo el proyecto

mvn clean test



# Tests de un microservicio específico  

cd api-user

mvn test

```



## 📚 API Documentation



### Endpoints Principales



| Endpoint | Servicio | Documentación |

|----------|----------|---------------|

| `/api/v1/user/**` | api-user | [Swagger](http://localhost:8081/swagger-ui/index.html#/) |

| `/api/v1/booking/**` | api-booking | [Swagger](http://localhost:8082/swagger-ui/index.html#/) |



### Postman Collections



Las colecciones de Postman están disponibles en cada microservicio:

- `api-user/postman/` - Colección de usuarios

- `api-booking/postman/` - Colección de reservas (próximamente)



## 🔄 Roadmap



### Fase 1 - Core Services (Actual)



- ✅ **api-user** - CRUD usuarios completo

- 🔄 **api-booking** - CRUD reservas (en desarrollo)

- ✅ **Infraestructura básica** - Eureka, Gateway, Config Server



### Fase 2 - Payment & Testing (Q1 2025)



- 📋 **api-payment** - Procesamiento pagos con MySQL

- 📋 **Resilience4j Integration** - Circuit Breaker, Retry, Rate Limiter

- 📋 **RabbitMQ Events** - api-payment → queue → api-booking (async)

- 📋 **Zipkin Tracing** - Trazabilidad distribuida para pruebas de carga

- 📋 **Load Testing** - Pruebas de estrés y observabilidad



### Fase 3 - Flight Services (Q2 2025)



- 📋 **api-flight** - Catálogo de vuelos (MongoDB)

- 📋 **api-flight-details** - Detalles de vuelos (MongoDB)

- 📋 **api-seat** - Gestión de asientos (MongoDB + Feign Server)

- 📋 **Feign Client Integration** - api-booking ↔ api-seat (sync)



### Fase 4 - Security & Production (Q3 2025)



- 📋 **Keycloak Integration** - Autenticación centralizada (puerto 8180)

- 📋 **MongoDB Clusters** - Bases de datos NoSQL distribuidas

- 📋 **Production Ready** - Monitoring, logging, CI/CD



## 🌐 URLs de Desarrollo



### Servicios Core

- **Eureka Dashboard:** http://localhost:8761

- **API Gateway:** http://localhost:8080

- **Gateway Health:** http://localhost:8080/actuator/health



### Microservicios Actuales

- **API User:** http://localhost:8081/swagger-ui/index.html#/

- **API User Health:** http://localhost:8081/actuator/health



### Microservicios Futuros

- **API Booking:** http://localhost:8082/swagger-ui/index.html#/ (en desarrollo)

- **API Payment:** http://localhost:8083/swagger-ui/index.html#/ (planeado)

- **API Flight:** http://localhost:8084/swagger-ui/index.html#/ (planeado)



### Infraestructura  

- **Zipkin UI:** http://localhost:9411 (para pruebas de carga)

- **RabbitMQ Management:** http://localhost:15672 (admin/admin123)



### Bases de Datos

- **MySQL User DB:** localhost:3310 (user_db)

- **MySQL Booking DB:** localhost:3311 (booking_db)

- **MySQL Payment DB:** localhost:3312 (payment_db) - Futuro



## 🧪 Testing & Quality



### Coverage Actual

- **api-user:** Tests implementados con JUnit 5 + Mockito

- **api-booking:** Tests en desarrollo

- **Integración:** Pendiente



### Estrategia de Testing

- **Unit Tests:** JUnit 5 + Mockito por microservicio

- **Integration Tests:** Pendiente evaluación

- **E2E Tests:** Postman Collections



## 🚨 Estado del Proyecto



### ✅ Completado

- Arquitectura base de microservicios

- Service Discovery con Eureka

- API Gateway configurado

- Microservicio api-user funcionando al 100%

- Docker Compose para desarrollo

- Documentación técnica completa



### 🔄 En Desarrollo  

- Microservicio api-booking (CRUD básico)



### 📋 Pendiente

- **Sistema de pagos** (api-payment + Resilience4j + MySQL)

- **Pruebas de carga** - Load testing con Zipkin tracing

- **Cola de mensajería** - RabbitMQ integration (payment → booking)

- **Catálogo de vuelos** (api-flight + MongoDB) 

- **Gestión de asientos** (api-seat + MongoDB + Feign Server)

- **Detalles de vuelos** (api-flight-details + MongoDB)

- **Comunicación síncrona** - Feign Client (booking ↔ seat)

- **Implementación de Keycloak** - Auth centralizada

- **Circuit Breaker patterns** - Resilience4j en api-payment



## 🤝 Proyecto Personal



Este es un **proyecto personal de aprendizaje y práctica**. Desarrollado para experimentar con arquitecturas de microservicios y tecnologías modernas de Spring Boot.



### 🎯 Propósito

- Aprendizaje hands-on de microservicios

- Experimentación con patrones distribuidos

- Portfolio técnico personal

- Práctica de tecnologías enterprise



## 🎯 Objetivos de Aprendizaje



- ✅ **Microservicios:** Arquitectura distribuida

- ✅ **Spring Cloud:** Eureka, Gateway, Config Server  

- ✅ **Docker:** Containerización y orquestación

- 🔄 **Testing:** Unitarios, integración, pruebas de carga

- 🔄 **Resilience Patterns:** Circuit Breaker, Retry, Rate Limiter

- 📋 **Message Queues:** RabbitMQ para comunicación asíncrona

- 📋 **Distributed Tracing:** Zipkin para observabilidad

- 📋 **Load Testing:** Pruebas de estrés y performance

- 📋 **NoSQL:** MongoDB para datos no relacionales

- 📋 **Service Communication:** Feign Client para REST síncronas

- 📋 **Seguridad:** Keycloak para autenticación centralizada

- 📋 **CI/CD:** GitLab para integración continua (futuro)



## 📞 Contacto



- **Desarrollador:** Erika Martínez

- **Email:** erimtz2@gmail.com

- **LinkedIn:** [linkedin.com/in/erika-daniela-martinez](https://linkedin.com/in/erika-daniela-martinez)

- **Proyecto:** Práctica de Microservicios con Spring Boot



## 📄 Licencia



Este proyecto es de código abierto para fines educativos - MIT License



---



⭐ **Si te gusta este proyecto, dale una estrella!** ⭐



🚀 **WingTrip - Volando hacia el futuro de los microservicios** 🚀

