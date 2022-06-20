package chat.api.user.controller;

import chat.api.user.jwt.TokenProvider;
import chat.api.user.model.Login;
import chat.api.user.model.Response;
import chat.api.user.model.SignupUser;
import chat.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    private final UserService userService;

    @PostMapping("/signup")
    public Response signup(@RequestBody @Valid SignupUser signupUser) {
        userService.signup(signupUser);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/login")
    public Response<Object> login(@RequestBody Login login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(login.getUserId(), login.getPassword());

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .data(tokenProvider.createToken(authenticate))
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    private Response violationException() {
        return Response.builder()
                .status(HttpStatus.CONFLICT.value())
                .message("email already exists.")
                .build();
    }
}
