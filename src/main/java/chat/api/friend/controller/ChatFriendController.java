package chat.api.friend.controller;

import chat.api.common.argumentresolver.RequestUser;
import chat.api.common.model.Response;
import chat.api.user.dto.UserDto;
import chat.api.friend.service.ChatFriendService;
import chat.api.user.entity.User;
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
    public Response<List<UserDto>> getFriends(@Parameter(hidden = true) @RequestUser User user) {
        return Response.of(chatFriendService.getFriends(user.getId()));
    }
}
