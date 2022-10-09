package chat.api.controller;

import chat.api.argumentresolver.User;
import chat.api.model.Response;
import chat.api.model.UserDto;
import chat.api.service.ChatFriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/friends")
@RestController
public class ChatFriendController {

    private final ChatFriendService chatFriendService;

    @Operation(summary = "친구 목록")
    @GetMapping
    public Response<List<UserDto>> getFriends(@Parameter(hidden = true) @User UserDto user) {
        return Response.of(chatFriendService.getFriends(user.getUserId()));
    }
}
