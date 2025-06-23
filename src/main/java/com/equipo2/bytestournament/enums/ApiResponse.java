package com.equipo2.bytestournament.enums;

import org.springframework.http.HttpStatus;

/**
 * Enumeración de errores de la API con sus respectivos códigos de estado HTTP, títulos y detalles.
 * Esta enumeración se utiliza para estandarizar los mensajes de error devueltos por la API.
 */
public enum ApiResponse {
    CREATED(
            HttpStatus.CREATED,
            "Recurso Creado",
           "El recurso se ha creado correctamente."),
    SUCCESS(
        HttpStatus.OK,
            "Operación Exitosa",
           "La operación se ha completado con éxito."),
    AUTHENTICATION_FAILED(
            HttpStatus.UNAUTHORIZED,
            "Autenticación Fallida",
            "Credenciales Inválidas"),
    NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "Recurso no encontrado",
            "El recuros solicitado no existe en el sistema."),
    BAD_REQUEST(
            HttpStatus.BAD_REQUEST,
            "Petición Incorrecta",
            "La solicitud no se pudo procesar debido a datos incorrectos o mal formados."),
    FORBIDDEN(
            HttpStatus.FORBIDDEN,
            "Acción Prohibida",
            "Permisos insuficientes para realizar esta acción."),
    FORBIDDEN_CREATE_ADMIN(
            HttpStatus.FORBIDDEN,
            "Acción Prohibida",
            "No se puede crear un usuario con rol de administrador sin permisos adecuados."),
    RECORD_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "Registro No Encontrado",
            "El registro solicitado no existe en la base de datos."),
    METHOD_NOT_ALLOWED(
            HttpStatus.METHOD_NOT_ALLOWED,
            "Método No Permitido",
            "El método HTTP utilizado no está permitido para este recurso."),
    CONFLICT(
            HttpStatus.CONFLICT,
            "Conflicto de Recursos",
            "La solicitud no se puede completar debido a un conflicto con el estado actual del recurso."),
    DUPLICATE_RESOURCE(
            HttpStatus.CONFLICT,
            "Recurso Duplicado",
            "El recurso que intenta crear ya existe."),
    UNPROCESSABLE_ENTITY(
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Entidad No Procesable",
            "La solicitud contiene datos que no se pueden procesar debido a errores de validación."),
    ASSOCIATED_RESOURCES(
            HttpStatus.CONFLICT,
            "Recurso Asociado",
            "No se puede eliminar el recurso porque tiene dependencias asociadas."),
    REQUEST_TIMEOUT(
            HttpStatus.REQUEST_TIMEOUT,
            "Operación Expirada",
            "La operación ha tardado demasiado tiempo y ha expirado."),
    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Error Interno del Servidor",
            "Se ha producido un error inesperado en el servidor."),
    EXTERNAL_API_ERROR(
            HttpStatus.SERVICE_UNAVAILABLE,
            "Servicio No Disponible",
            "El servicio externo requerido no está disponible temporalmente."),
    ENDPOINT_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "Endpoint No Encontrado",
            "El endpoint solicitado de la API no existe.");

    /**
    * status: El código de estado HTTP asociado a la respuesta de la API.
    * title: Un título breve que describe el tipo de error o éxito de la respuesta.
    * detail: Un detalle más específico que proporciona información adicional sobre la respuesta.  
    */
    private final HttpStatus status;
    private final String title;
    private final String detail;

    private ApiResponse(HttpStatus status, String title, String detail) {
        this.status = status;
        this.title = title;
        this.detail = detail;
    }

    public HttpStatus getStatus() { return status; }

   
    public String getTitle() { return title; }

    public String getDetail() { return detail; }
}