package chat.api.controller;

import chat.api.argumentresolver.User;
import chat.api.model.ChatMessageDto;
import chat.api.model.ChatRoomDto;
import chat.api.model.Response;
import chat.api.service.ChatService;
import chat.api.model.UserDto;
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
    public Response<List<ChatMessageDto>> getMessage(@PathVariable Long roomId, @Parameter(hidden = true) @User UserDto user) {
        return Response.of(chatService.getMessages(roomId, user.getUserId()));
    }

    @Operation(summary = "친구 목록")
    @GetMapping("/friends")
    public Response<List<UserDto>> getFriends(@Parameter(hidden = true) @User UserDto user) {
        return Response.of(chatService.getFriends(user.getUserId()));
    }

    @PutMapping("/rooms/{roomId}/users/{userId}/last-message-id/{lastMessageId}")
    public void modifyLastMessageId(@PathVariable Long roomId,
                                    @PathVariable Long userId,
                                    @PathVariable Long lastMessageId) {
        chatService.modifyLastMessageId(roomId, userId, lastMessageId);
    }
}
