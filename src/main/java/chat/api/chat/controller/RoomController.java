package chat.api.chat.controller;

import chat.api.chat.argumentresolver.User;
import chat.api.chat.model.ChatRoomDto;
import chat.api.chat.service.RoomService;
import chat.api.user.model.UserDto;
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
    public List<ChatRoomDto> getRooms(@User UserDto user) {
        System.out.println("user = " + user);

        return roomService.getAllRoom(user.getUserId());
    }
}
