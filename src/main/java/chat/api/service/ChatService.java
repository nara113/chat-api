package chat.api.service;

import chat.api.entity.ChatGroup;
import chat.api.entity.ChatMessage;
import chat.api.entity.ChatRoom;
import chat.api.entity.User;
import chat.api.mapper.ChatMapper;
import chat.api.model.*;
import chat.api.repository.*;
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
                .senderName(messageDto.getSenderName())
                .message(messageDto.getMessage())
                .user(user)
                .chatRoom(chatRoom)
                .build();

        chatMessageRepository.save(chatMessage);

        return chatMessage.getId();
    }

    public List<ChatMessageDto> getMessages(Long roomId, Long userId) {
        return chatMessageRepository.findByChatRoomIdOrderById(roomId)
                .stream()
                .map(ChatMessageDto::new)
                .collect(Collectors.toList());
    }

    public List<UserDto> getFriends(Long userId) {
        return chatFriendRepository.findFriendByUserIdAndBlockYn(userId, "N")
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public List<ChatGroup> getGroupsByRoomId(Long roomId) {
        return chatGroupRepository.findByChatRoomId(roomId)
                .stream()
                .collect(Collectors.toList());
    }

    public List<LastReadMessageDto> getLastReadMessagesByRoomId(Long roomId) {
        return chatGroupRepository.findByChatRoomId(roomId)
                .stream()
                .map(LastReadMessageDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long updateToLastMessage(Long roomId, Long userId) {
        ChatGroup chatGroup =
                chatGroupRepository.findByChatRoomIdAndUserId(roomId, userId)
                        .orElseThrow(() -> new IllegalArgumentException("chatGroup does not exist. room id : " + roomId));

        Long lastMessageId = chatMessageRepository.findLastMessageByRoomId(roomId)
                .orElse(0L);

        chatGroup.setLastReadMessageId(lastMessageId);

        return lastMessageId;
    }

    public void markAsRead(ReadMessageDto readMessageDto) {
        chatGroupRepository.updateLastReadMessageId(readMessageDto.getRoomId(), readMessageDto.getUserId(), readMessageDto.getMessageId());
    }
}
