package chat.api.chat.service;

import chat.api.chat.entity.ChatGroup;
import chat.api.chat.entity.ChatMessage;
import chat.api.chat.entity.ChatRoom;
import chat.api.chat.mapper.ChatMapper;
import chat.api.chat.model.ChatMessageDto;
import chat.api.chat.model.ChatRoomDto;
import chat.api.chat.repository.ChatGroupRepository;
import chat.api.chat.repository.ChatMessageRepository;
import chat.api.chat.repository.ChatRoomRepository;
import chat.api.user.entity.User;
import chat.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    public List<ChatMessageDto> getMessages(Long roomId) {
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
}
