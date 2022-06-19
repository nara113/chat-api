package chat.api.controller;

import chat.api.model.ChatRoomDto;
import chat.api.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
@RestController
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public List<ChatRoomDto> getRooms() {
        return roomService.getAllRoom(1L);
    }
}
