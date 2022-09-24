package chat.api.controller;

import chat.api.model.*;
import chat.api.service.UserService;
import chat.api.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public Response<String> signup(@RequestBody @Valid SignupUser signupUser) {
        userService.signup(signupUser);

        return Response.of(HttpStatus.CREATED.value(), "success");
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public Response<LoginResponseData> login(@RequestBody Login login) {
        return Response.of(
                LoginResponseData.builder()
                .token(userService.getToken(login))
                .user(new UserDto(userService.getUserByEmail(login.getEmail())))
                .build());
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public Response<String> logout(HttpServletRequest request) {
        String jwt = TokenUtil.resolveToken(request);

        if (StringUtils.isNotEmpty(jwt)) {
            userService.logout(jwt);
        }

        return Response.of("success");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    private Response<String> violationException() {
        return Response.of(HttpStatus.CONFLICT.value(), "email already exists.");
    }
}
