package chat.api.service;

import chat.api.entity.*;
import chat.api.mapper.ChatGroupMapper;
import chat.api.mapper.RoomMapper;
import chat.api.model.*;
import chat.api.model.request.CreateRoomRequest;
import chat.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final RoomMapper roomMapper;

    private final ChatMessageRepository chatMessageRepository;

    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatGroupRepository chatGroupRepository;

    private final ChatGroupMapper chatGroupMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<ChatRoomDto> getAllRoom(Long userId) {
        return roomMapper.selectAllRoomsByUserId(userId);
    }

    public ChatRoomDto getRoom(Long userId, Long roomId) {
        return roomMapper.selectRoomByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() ->
                        new IllegalArgumentException("room does not exist. room id : " + roomId + " user id : " + userId));
    }

    @Transactional
    public Long saveChatMessage(ChatMessageDto messageDto) {
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(messageDto.getRoomId());
        User user = userRepository.getReferenceById(messageDto.getSenderId());

        ChatMessage chatMessage = ChatMessage.builder()
                .message(messageDto.getMessage())
                .user(user)
                .chatRoom(chatRoom)
                .build();

        chatMessageRepository.save(chatMessage);

        return chatMessage.getId();
    }

    public List<ChatMessageDto> getMessages(Long roomId, Long lastMessageId) {
        List<ChatMessage> messages;

        if (lastMessageId == null) {
            messages = chatMessageRepository.findTop30ByChatRoomIdOrderByIdDesc(roomId);
        } else {
            messages = chatMessageRepository.findTop30ByChatRoomIdAndIdIsLessThanOrderByIdDesc(roomId, lastMessageId);
        }

        return messages.stream()
                .map(ChatMessageDto::new)
                .collect(toList());
    }

    public List<ChatGroup> getGroupsByRoomId(Long roomId) {
        return chatGroupRepository.findByChatRoomId(roomId);
    }

    public List<LastReadMessageDto> getLastReadMessagesByRoomId(Long roomId) {
        return chatGroupRepository.findByChatRoomId(roomId)
                .stream()
                .map(LastReadMessageDto::new)
                .collect(toList());
    }

    @Transactional
    public Long updateToLastMessage(Long roomId, Long userId) {
        ChatGroup chatGroup =
                chatGroupRepository.findByChatRoomIdAndUserId(roomId, userId)
                        .orElseThrow(() -> new IllegalArgumentException("chatGroup does not exist. room id : " + roomId));

        Long lastMessageId = chatMessageRepository.findLastMessageByRoomId(roomId)
                .orElse(0L);

        chatGroup.changeLastReadMessageId(lastMessageId);

        return lastMessageId;
    }

    public void markAsRead(ReadMessageDto readMessageDto) {
        chatGroupRepository.updateLastReadMessageId(
                readMessageDto.getRoomId(),
                readMessageDto.getUserId(),
                readMessageDto.getLastMessageId());
    }

    @Transactional
    public void join(Long roomId, Long inviterId, List<Long> invitedUserIds) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("room does not exist. room id: " + roomId));

        chatGroupRepository.findByChatRoomIdAndUserId(roomId, inviterId)
                .orElseThrow(() -> new IllegalArgumentException("chatGroup does not exist. room id : " + roomId));

        List<User> users = invitedUserIds
                .stream()
                .map(userRepository::getReferenceById)
                .collect(toList());

        chatRoom.addUsers(users);
    }

    @Transactional
    public void enter(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(roomId);
        User user = userRepository.getReferenceById(userId);

        ChatGroup chatGroup = ChatGroup
                .builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();

        chatGroupRepository.save(chatGroup);
    }

    @Transactional
    public void createRoom(Long userId, CreateRoomRequest request) {
        if (!request.getParticipantUserIds().contains(userId)) {
            throw new IllegalArgumentException("Room creator must always be included as a participant.");
        }

        ChatRoom room = ChatRoom.builder()
                .name(request.getRoomName())
                .build();

        chatRoomRepository.save(room);

        chatGroupMapper.insertChatGroups(room.getId(), request.getParticipantUserIds());
    }

}
