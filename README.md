# bytes-tournament

## Descripción del Proyecto 👑

Bytes Tournament es un sistema backend para la organización y gestión de torneos de videojuegos online. La plataforma proporciona una API REST completa que permite a los usuarios crear torneos, registrarse para participar, competir y seguir los resultados en tiempo real. El sistema incluye funcionalidades como emparejamiento automático de jugadores, sistema de chat básico, clasificaciones dinámicas y actualización de resultados, todo construido con un enfoque en la robustez, seguridad y preparación para entornos de producción.

## Metodología de Trabajo 🛠️

Este proyecto sigue la metodología ágil **Scrum** para la gestión y desarrollo de las tareas.  
Utilizamos **Trello** para organizar y hacer seguimiento de las tareas, asegurando una colaboración eficiente y una entrega continua de valor.

## Equipo de Desarrollo 👥

@karlosvas  
@JariJariLo  
@Rs-845  
@ChristianEscalas

Creadores de Bytes Colaborativos:  
@devch-tech y @Jorexbp  
Canal de Twitch: [Bytes Colaborativos](https://www.twitch.tv/api/bytescolaborativos)

## Funcionalidades Principales 🧩

- **Gestión de usuarios**: Sistema completo con registro, autenticación y roles diferenciados (administrador, organizador, jugador).
- **Gestión de torneos**: Creación, edición, consulta y eliminación de torneos con diferentes formatos y configuraciones.
- **Sistema de registro**: Inscripción de jugadores en torneos con validación de requisitos.
- **Emparejamiento automático**: Algoritmo inteligente para emparejar jugadores según nivel, historial y otros parámetros.
- **Chat básico**: Comunicación entre jugadores mediante sistema HTTP para coordinación de partidas.
- **Clasificaciones dinámicas**: Tablas de clasificación actualizadas en tiempo real según resultados.
- **Seguimiento de resultados**: Registro y actualización de resultados con histórico de partidas.
- **API REST documentada**: Endpoints bien definidos y documentados para facilitar la integración.
- **Seguridad robusta**: Control de acceso basado en roles con autenticación JWT.

## Tecnologías Utilizadas 🐧🐋

- **Lenguaje**: Java 17
- **Framework**: Spring Boot (MVC)
- **Seguridad**: Spring Security + JWT
- **Base de datos**: PostgreSQL
- **Documentación de API**: SwaggerUI
- **Testing**:
  - Postman para pruebas de integración
  - JUnit/Mockito para pruebas unitarias
- **Control de versiones**: Git (GitHub)
- **Gestión de proyecto**: Trello (metodología Scrum)
- **Contenedores**: Docker (para desarrollo y despliegue)

## Instalación y Configuración 🚀

### Requisitos Previos

- Java 17 instalado
- PostgreSQL instalado y configurado
- Docker (opcional, para contenedores)
- Maven instalado (mvn)
- Archivo .env configurado con las variables necesarias

### Pasos para la Instalación

```bash
  // Clonar el repositorio
  git clone https://github.com/karlosvas/bytes-tournament.git
  cd bytes-tournament

  // IMPORTANTE: Crear un archivo .env en la raíz del proyecto con las variables del .env.demo:

  mvn clean install // O mvn clean package

  // Dos opciones:
  // 1. Ejecutar con Doker
  docker-compose up
  // 2. Ejecutar directamente, es lo mismo que (Boton de inicio)
  mvn spring-boot:run

  // Ya podrás acceder a la API en http://localhost:8081/api
```

## Documentación de la API 📖

### SwaggerUI

La documentación de la API está disponible en SwaggerUI. Puedes acceder a ella al iniciar la applicación en la url: `http://localhost:8081/swagger-ui/index.html`

### JavaDoc

La documentación de JavaDoc está disponible en el directorio `target/site/apidocs` después de compilar el proyecto. Puedes abrir el archivo `index.html` en un navegador para explorar la documentación de las clases y métodos.
Para generar la documentación de JavaDoc, ejecuta el siguiente comando:

```bash
mvn javadoc:javadoc
```
