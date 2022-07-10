package chat.api.controller;

import chat.api.model.*;
import chat.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public Response<String> signup(@RequestBody @Valid SignupUser signupUser) {
        userService.signup(signupUser);

        return Response.of("success");
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public Response<LoginResponseData> login(@RequestBody Login login) {
        return Response.of(LoginResponseData.builder()
                .token(userService.getToken(login))
                .user(new UserDto(userService.getUser(login.getEmail())))
                .build());
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public void logout(@RequestBody Login login) {
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    private Response<String> violationException() {
        return Response.of(HttpStatus.CONFLICT.value(), "email already exists.");
    }
}
