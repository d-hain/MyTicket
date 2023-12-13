package me.dave.myticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.dave.Permissions;
import me.dave.myticket.dto.UserSigninDto;
import me.dave.myticket.dto.UserSignupDto;
import me.dave.myticket.dto.UserResponseDto;
import me.dave.myticket.dto.UserUpdateDto;
import me.dave.myticket.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(
        summary = "Sign up a new user",
        description = "This can be done by everyone including guests",
        tags = {"user", "signup"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Successful operation"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Failed to sign up"
        )
    })
    @PostMapping("/signup")
    @Permissions(user = true, guest = true)
    public ResponseEntity<Void> signupUser(
        @RequestBody UserSignupDto user
    ) {
        String token = service.signup(user);
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .build();
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(
        summary = "Sign in a user",
        description = "This can be done by everyone including guests",
        tags = {"user", "signin", "login"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Failed to sign in"
        )
    })
    @PostMapping("/signin")
    @Permissions(user = true, guest = true)
    public ResponseEntity<Void> signinUser(
        @RequestBody UserSigninDto user
    ) {
        String token = service.signin(user);
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .build();
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(
        summary = "Update an existing user",
        description = "This can only be done by an admin",
        tags = {"user", "update"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to update user"
        )
    })
    @PutMapping("/update")
    @Permissions(user = false)
    public ResponseEntity<Void> updateUser(
        @RequestBody UserUpdateDto user
    ) {
        if (service.update(user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
        summary = "Delete a user",
        description = "This can only be done by an admin",
        tags = {"user", "delete"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to delete user"
        )
    })
    @DeleteMapping("/delete/{id}")
    @Permissions(user = false)
    public ResponseEntity<Void> deleteUser(
        @PathVariable Long id
    ) {
        if (service.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
        summary = "List all users",
        description = "This can only be done by an admin",
        tags = {"user", "list"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to list users"
        )
    })
    @GetMapping("/list")
    @Permissions(user = false)
    public ResponseEntity<List<UserResponseDto>> listUsers() {
        List<UserResponseDto> users = service.list();
        if (users != null) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
        summary = "Load a specific user",
        description = "This can only be done by an admin",
        tags = {"user", "load"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to load user"
        )
    })
    @GetMapping("/load/{id}")
    @Permissions(user = false)
    public ResponseEntity<UserResponseDto> listUser(
        @PathVariable Long id
    ) {
        UserResponseDto user = service.load(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
