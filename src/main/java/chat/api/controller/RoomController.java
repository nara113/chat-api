package chat.api.controller;

import chat.api.model.ChatRoom;
import chat.api.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
@RestController
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/rooms")
    public List<ChatRoom> room() {
        return roomService.getAllRoom();
    }

    @PostMapping("/room")
    public ChatRoom createRoom(@RequestParam String name) {
        return roomService.createRoom(name);
    }

    @GetMapping("/room/{roomId}")
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return roomService.getRoom(roomId);
    }
}
