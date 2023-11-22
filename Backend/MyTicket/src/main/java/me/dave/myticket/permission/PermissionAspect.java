package me.dave.myticket.permission;

import me.dave.myticket.model.User;
import me.dave.myticket.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class PermissionAspect {
    private final UserService userService;
    private String fullMethodName = "";

    public PermissionAspect(UserService userService) {
        this.userService = userService;
    }

    @Around("@annotation(me.dave.myticket.permission.Permissions)")
    public ResponseEntity<?> checkPermissions(ProceedingJoinPoint joinPoint) throws Throwable {
        fullMethodName = joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature().getName();
        
        // Has to be a method in a class annotated with @RestController
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> methodClass = signature.getDeclaringType();
        if (!methodClass.isAnnotationPresent(RestController.class)) {
            System.err.printf(
                """
                    Permissions:\s
                    Method "%s" annotated with @Permissions has to be in a class annotated with @RestController
                    """,
                fullMethodName
            );
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Get permission settings from the annotation
        Permissions permissions = method.getAnnotation(Permissions.class);

        // Just run the method if the permissions are set to allow guests
        if (permissions.guest()) {
            return (ResponseEntity<?>) joinPoint.proceed(joinPoint.getArgs());
        }

        if (permissions.user()) {
            String bearerToken = getMethodBearerToken(method, joinPoint, "user = true");
            if (bearerToken == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (!isTokenValid(bearerToken, "user = true")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Continue with the method if everything looks good
            return (ResponseEntity<?>) joinPoint.proceed(joinPoint.getArgs());
        }

        if (permissions.admin()) {
            String bearerToken = getMethodBearerToken(method, joinPoint, "admin = true");
            if (bearerToken == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (!isTokenValid(bearerToken, "admin = true")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // proceed the method if the user has admin permissions
            User user = userService.getByToken(bearerToken);
            if (user.getFirstname().equals("admin") && user.getLastname().equals("admin")) {
                return (ResponseEntity<?>) joinPoint.proceed(joinPoint.getArgs());
            }
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isTokenValid(String bearerToken, String atPermission) {
        if (bearerToken.isEmpty()) {
            printTokenEmptyError(atPermission);
            return false;
        }
        if (!userService.isValidToken(bearerToken)) {
            System.err.println("Permissions: Invalid bearer token");
            return false;
        }

        return true;
    }

    /**
     * @param atPermission The permission setting from the annotation
     * @return The Bearer token in the Authorization header or null if there is none
     */
    private String getMethodBearerToken(Method method, JoinPoint joinPoint, String atPermission) {
        if (joinPoint.getArgs().length == 0) {
            printNoRequestHeaderError(atPermission);
            return null;
        }

        List<Annotation[]> allParameterAnnotations = List.of(method.getParameterAnnotations());
        for (Annotation[] paramAnnotations : allParameterAnnotations) {
            for (Annotation paramAnnotation : paramAnnotations) {
                if (paramAnnotation instanceof RequestHeader) {
                    // if it is an Authorization header, get the token
                    String authorization = ((RequestHeader) paramAnnotation).value();
                    if (authorization.equals("Authorization")) {
                        int paramAnnotationsIndex = allParameterAnnotations.indexOf(paramAnnotations);
                        if (paramAnnotationsIndex == -1) {
                            printNoRequestHeaderError(atPermission);
                            return null;
                        }

                        return (String) joinPoint.getArgs()[paramAnnotationsIndex];
                    }
                }
            }
        }

        printNoRequestHeaderError(atPermission);
        return null;
    }

    private void printTokenEmptyError(String atPermission) {
        System.err.printf(
            """
                Permissions:\s
                Method "%s" annotated with @Permissions(%s) but no Authorization header found
                """,
            fullMethodName,
            atPermission
        );
    }

    private void printNoRequestHeaderError(String atPermission) {
        System.err.printf(
            """
                Permissions:\s
                Method "%s" annotated with @Permissions(%s) has to have a parameter annotated with @RequestHeader("Authorization")
                """,
            fullMethodName,
            atPermission
        );
    }
}
