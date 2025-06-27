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
  docker-compose up --build // La primera vez que lo ejecutas o si cambias el código utiliza --build

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

## Manual basico de usuario

Si quieres utilizar la api deves tener en cuenta unos cuantos puntos importantes:

- **Autenticación**: Utiliza el endpoint `/api/auth` para obtener un token JWT. Este token debe ser incluido en las cabeceras de las peticiones a los endpoints protegidos.

  - **Registro**: `/api/auth/register` Incluye el token JWT en la cabecera `Authorization` de tus peticiones, con el formato `Bearer <token>`.
  - **Login**: `/api/auth/login` Incluye el token JWT en la cabecera `Authorization` de tus peticiones, con el formato `Bearer <token>`.
  - **Admin** `/api/auth/register` para crear un usuario administrador deves de ser administrador o tener el permiso `USER_CREATE`, si no tienes acceso a un administrador puedes utilizar en administrador creado por defecto con los parametrso que utilices en tu env

- **Entorno** Pero y si no sabes que valores deven utilizare en el .env? en este mismo repositorio en la raíz del proyetco se encuentra un archivo `.env.demo` que contiene los valores de ejemplo que puedes utilizar para crear tu propio archivo `.env`, simplemente renombralo a `.env` y edita los valores según tus necesidades.
  También deves tener en cuenta que esto solo ocurre cuando utilizamos docker.

- **Entorno Local** Si estas utilizando el entorno local, sin utilizar docker con el comando `mvn spring-boot:run` o con el botón de play, no es necesario crear un archivo `.env`, ya que las variables de entorno se configuran directamente en el archivo `application.properties` y `application-dev.propperties`.

- **Testing con Postman** Como pueso obtener los enpoints necesarios para hacer testing?
  Puedes utilizar Postman para probar todos los endpoints de la API. Estos archivos se encuentran en el directorio `postman` del proyecto. Puedes importar el archivo `bytes-tournament.postman_collection.json` en Postman para acceder a todos los endpoints y realizar pruebas, y `Bytes_Tournament.postman_environment` para variables de entorno, tambien puedes ver yu probar los enpoints desde Swagger.

- **Testing con JUnit y Mockito** Si quieres hacer pruebas unitarias puedes utilizar JUnit y Mockito. Puedes ejecutar las pruebas utilizando el comando `mvn test` o desde tu IDE.

- **La base de datos** Si quieres saber más sobre la base de datos, puedes consultar el archivo `src/main/resources/tournament_db.sql`. Este archivo contiene la estructura de la base de datos y las tablas necesarias para el funcionamiento de la API y que crea docker.

> **Nota:** Si encuentras algún bug, por favor abre una issue en el repositorio de GitHub para que podamos solucionarlo lo antes posible. ¡Gracias de antemano por tu colaboración! 🐛
