package me.dave.myticket.permission;

import jakarta.servlet.http.HttpServletRequest;
import me.dave.myticket.model.Role;
import me.dave.myticket.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Objects;

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
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // Get permission settings from the annotation
        Permissions permissions = method.getAnnotation(Permissions.class);

        // Just run the method if the permissions are set to allow guests
        if (permissions.guest()) {
            return (ResponseEntity<?>) joinPoint.proceed(joinPoint.getArgs());
        }

        if (permissions.user()) {
            String bearerToken = getMethodBearerToken();

            if (!isTokenValid(bearerToken, Role.USER, "user = true")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Continue with the method if everything looks good
            return (ResponseEntity<?>) joinPoint.proceed(joinPoint.getArgs());
        }

        if (permissions.admin()) {
            String bearerToken = getMethodBearerToken();

            if (!isTokenValid(bearerToken, Role.ADMIN, "admin = true")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            return (ResponseEntity<?>) joinPoint.proceed(joinPoint.getArgs());
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isTokenValid(String bearerToken, Role role, String atPermission) {
        if (bearerToken.isEmpty()) {
            printTokenEmptyError(atPermission);
            return false;
        }
        if (!userService.isValidToken(bearerToken, role)) {
            System.err.println("Permissions: Invalid bearer token");
            return false;
        }

        return true;
    }

    private String getMethodBearerToken() {
        HttpServletRequest request =
            ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        return request.getHeader("Authorization");
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
}
