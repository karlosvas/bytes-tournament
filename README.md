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
