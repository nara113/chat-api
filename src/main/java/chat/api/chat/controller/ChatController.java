package chat.api.chat.controller;

import chat.api.chat.argumentresolver.User;
import chat.api.chat.model.ChatMessageDto;
import chat.api.chat.model.ChatRoomDto;
import chat.api.chat.service.ChatService;
import chat.api.user.model.UserDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/rooms")
    public List<ChatRoomDto> getRooms(@Parameter(hidden = true) @User UserDto user) {
        System.out.println("user = " + user);

        return chatService.getAllRoom(user.getUserId());
    }

    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatMessageDto> getMessage(@PathVariable Long roomId) {
        return chatService.getMessages(roomId);
    }

    @PutMapping("/rooms/{roomId}/users/{userId}/last-message-id/{lastMessageId}")
    public void modifyLastMessageId(@PathVariable Long roomId,
                                    @PathVariable Long userId,
                                    @PathVariable Long lastMessageId) {
        chatService.modifyLastMessageId(roomId, userId, lastMessageId);
    }
}
