package chat.api.controller;

import chat.api.argumentresolver.User;
import chat.api.model.*;
import chat.api.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅방 목록")
    @GetMapping("/rooms")
    public Response<List<ChatRoomDto>> getRooms(@Parameter(hidden = true) @User UserDto user) {
        return Response.of(chatService.getAllRoom(user.getUserId()));
    }

    @Operation(summary = "채팅방 메시지 목록")
    @GetMapping("/rooms/{roomId}/messages")
    public Response<List<ChatMessageDto>> getMessages(@PathVariable Long roomId, @Parameter(hidden = true) @User UserDto user) {
        return Response.of(chatService.getMessages(roomId, user.getUserId()));
    }

    @Operation(summary = "채팅방 유저별 마지막 읽은 메시지 아이디 목록")
    @GetMapping("/rooms/{roomId}/last-read")
    public Response<List<LastReadMessageDto>> getUsersByRoom(@PathVariable Long roomId) {
        return Response.of(chatService.getLastReadMessagesByRoomId(roomId));
    }

    @Operation(summary = "친구 목록")
    @GetMapping("/friends")
    public Response<List<UserDto>> getFriends(@Parameter(hidden = true) @User UserDto user) {
        return Response.of(chatService.getFriends(user.getUserId()));
    }

    @Operation(summary = "채팅방 입장")
    @PostMapping("/rooms/{roomId}/user")
    public Response addUser(@Parameter(hidden = true) @User UserDto user, @PathVariable String roomId) {

        return null;
    }

    @Operation(summary = "채팅방 퇴장")
    @DeleteMapping("/rooms/{roomId}/user")
    public Response deleteUser(@Parameter(hidden = true) @User UserDto user, @PathVariable String roomId) {

        return null;
    }
}
