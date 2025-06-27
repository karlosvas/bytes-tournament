package com.equipo2.bytestournament.aspect;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.equipo2.bytestournament.utilities.Colours;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Aspecto para registrar información de las peticiones HTTP en los controladores REST.
 * Este aspecto intercepta las llamadas a los métodos de los controladores REST
 * y registra información relevante como el método HTTP, la ruta de la petición y los parámetros pasados.
 * {@link Aspect} Anotación que indica que esta clase es un aspecto de AOP (Programación Orientada a Aspectos).
 * {@link Component} Anotación que indica que esta clase es un componente de Spring y será registrada en el contexto de la aplicación.
 * {@link Logger} Utilizado para registrar mensajes de log, en este caso, para registrar información de las peticiones HTTP.
 * {@link LoggerFactory} Utilizado para crear instancias de Logger.
 */
@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Registra información antes de que se ejecute un método en un controlador REST, interceptando las llamadas a los métodos para 
     * registrar información relevante como el método HTTP, la ruta de la petición y los parámetros pasados.
     * 
     * @Before es un bean de Aspect que se ejecuta antes de la ejecución del método interceptado.
     * 
     * @param joinPoint El punto de unión que proporciona información sobre el método interceptado, incluyendo su firma y los argumentos pasados.
     */
    @Before("within(@RestController *)")
    public void logBefore(JoinPoint joinPoint) {
        String httpMethod = "UNKNOWN";
        String path = "UNKNOWN";

        // Obtener el método HTTP de la petición 
        if (joinPoint.getSignature() instanceof org.aspectj.lang.reflect.MethodSignature methodSignature) {
            var method = methodSignature.getMethod();
            if (method.isAnnotationPresent(GetMapping.class)) httpMethod = "GET";
            else if (method.isAnnotationPresent(PostMapping.class)) httpMethod = "POST";
            else if (method.isAnnotationPresent(PutMapping.class)) httpMethod = "PUT";
            else if (method.isAnnotationPresent(DeleteMapping.class)) httpMethod = "DELETE";
            else if (method.isAnnotationPresent(PatchMapping.class)) httpMethod = "PATCH";
            else if (method.isAnnotationPresent(RequestMapping.class)) {
                var mapping = method.getAnnotation(RequestMapping.class);
                if (mapping.method().length > 0) httpMethod = mapping.method()[0].name();
            }
        }

         // Obtener la ruta real de la petición HTTP
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            path = request.getRequestURI();
        }

        logger.info(Colours.paintGreen("\n{} {} \nParams: {}"), httpMethod, path, Arrays.toString(joinPoint.getArgs()));
    }
}