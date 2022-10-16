package chat.api.user.controller;

import chat.api.argumentresolver.User;
import chat.api.model.Response;
import chat.api.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    @Operation(summary = "현재 유저 정보")
    @GetMapping("/current")
    public Response<UserDto> getRooms(@Parameter(hidden = true) @User UserDto user) {
        return Response.of(user);
    }
}
