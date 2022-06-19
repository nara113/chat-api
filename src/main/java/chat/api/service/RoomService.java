package chat.api.service;

import chat.api.entity.ChatGroup;
import chat.api.model.ChatRoomDto;
import chat.api.model.UserDto;
import chat.api.repository.ChatGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final ChatGroupRepository chatGroupRepository;

    public List<ChatRoomDto> getAllRoom() {
        List<ChatGroup> groups = chatGroupRepository.findAllByUserId(1L)
                .orElseThrow(() -> new IllegalArgumentException());

        return groups.stream()
                .map(ChatGroup::getChatRoom)
                .map(room -> ChatRoomDto.builder()
                        .roomId(room.getId())
                        .users(room.getUsers()
                                .stream()
                                .map(user -> UserDto.builder().name(user.getUser().getName()).build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
