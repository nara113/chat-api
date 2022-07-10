package chat.api.service;

import chat.api.entity.ChatFriend;
import chat.api.entity.ChatGroup;
import chat.api.entity.ChatMessage;
import chat.api.entity.ChatRoom;
import chat.api.mapper.ChatMapper;
import chat.api.model.ChatMessageDto;
import chat.api.model.ChatRoomDto;
import chat.api.repository.ChatFriendRepository;
import chat.api.repository.ChatGroupRepository;
import chat.api.repository.ChatMessageRepository;
import chat.api.repository.ChatRoomRepository;
import chat.api.entity.User;
import chat.api.model.UserDto;
import chat.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatService {
    private final ChatMapper chatMapper;

    private final ChatMessageRepository chatMessageRepository;

    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatGroupRepository chatGroupRepository;

    private final ChatFriendRepository chatFriendRepository;

    public List<ChatRoomDto> getAllRoom(Long userId) {
        return chatMapper.selectAllRoomsByUserId(userId);
    }

    @Transactional
    public long saveChatMessage(ChatMessageDto messageDto) {
        User user = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("user does not exist. user id : " + messageDto.getSenderId()));

        ChatRoom chatRoom = chatRoomRepository.findById(messageDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("room does not exist. room id : " + messageDto.getRoomId()));

        ChatMessage chatMessage = ChatMessage.builder()
                .message(messageDto.getMessage())
                .user(user)
                .chatRoom(chatRoom)
                .build();

        chatMessageRepository.save(chatMessage);

        return chatMessage.getId();
    }

    public List<ChatMessageDto> getMessages(Long roomId, Long userId) {
        chatGroupRepository.findByChatRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("user does not exist in the chat room." +
                        " room id : " + roomId + " user id : " + userId));

        return chatMessageRepository.findByChatRoomIdOrderById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("room does not exist. room id : " + roomId))
                .stream()
                .map(chatMessage -> ChatMessageDto.builder()
                        .message(chatMessage.getMessage())
                        .messageId(chatMessage.getId())
                        .roomId(chatMessage.getChatRoom().getId())
                        .senderId(chatMessage.getUser().getId())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyLastMessageId(Long roomId, Long userId, Long lastMessageId) {
        ChatGroup chatGroup = chatGroupRepository.findByChatRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("chat group does not exist." +
                        " room id : " + roomId + " user id : " + userId));

        chatGroup.setLastReadMessageId(lastMessageId);
    }

    public List<UserDto> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user does not exist. user id : " + userId));

        return chatFriendRepository.findByUserAndBlockYn(user, "N")
                .orElseThrow(() -> new IllegalArgumentException("user does not exist. user id : " + userId))
                .stream()
                .map(ChatFriend::getFriend)
                .map(friend -> UserDto.builder()
                        .userId(friend.getId())
                        .name(friend.getName())
                        .email(friend.getEmail())
                        .build())
                .collect(Collectors.toList());
    }
}
