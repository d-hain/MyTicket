package org.dave;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized"
    )
})
/**
 * If a method is annotated with this annotation, the PermissionAspect will check if the user has the required permissions.
 * A user can automatically use a method if the method is annotated with @Permissions(guest = true).
 * And the same goes for @Permissions(user = true) for admins.
 */
public @interface Permissions {
    boolean admin() default true;
    boolean user();
    boolean guest() default false;
}
