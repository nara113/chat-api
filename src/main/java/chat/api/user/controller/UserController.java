package chat.api.user.controller;

import chat.api.user.model.Login;
import chat.api.user.model.response.Response;
import chat.api.user.model.SignupUser;
import chat.api.user.model.UserDto;
import chat.api.user.model.response.LoginResponseData;
import chat.api.user.service.UserService;
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

    @PostMapping("/signup")
    public Response signup(@RequestBody @Valid SignupUser signupUser) {
        userService.signup(signupUser);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/login")
    public Response login(@RequestBody Login login) {
        return Response.builder()
                .status(HttpStatus.OK.value())
                .data(LoginResponseData.builder()
                        .token(userService.getToken(login))
                        .user(new UserDto(userService.getUser(login.getEmail())))
                        .build())
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
