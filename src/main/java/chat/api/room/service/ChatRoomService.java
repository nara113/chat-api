package chat.api.room.service;

import chat.api.message.dto.ChatType;
import chat.api.room.entity.ChatGroup;
import chat.api.room.repository.ChatGroupRepository;
import chat.api.room.repository.mapper.ChatGroupMapper;
import chat.api.message.dto.ChatMessageDto;
import chat.api.message.dto.ReadMessageDto;
import chat.api.room.entity.ChatMessage;
import chat.api.room.repository.ChatMessageRepository;
import chat.api.room.dto.ChatRoomDto;
import chat.api.room.entity.ChatRoom;
import chat.api.room.repository.mapper.RoomMapper;
import chat.api.room.dto.request.CreateRoomRequest;
import chat.api.room.repository.ChatRoomRepository;
import chat.api.user.entity.User;
import chat.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private final SimpMessagingTemplate template;

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

        ChatMessage chatMessage = ChatMessage.createMessage(
                messageDto.getMessage(),
                user,
                chatRoom,
                ChatType.TALK
        );

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
                .toList();
    }

    public List<ChatGroup> getGroupsByRoomId(Long roomId) {
        return chatGroupRepository.findByChatRoomId(roomId);
    }

    public List<ReadMessageDto> getLastReadMessagesByRoomId(Long roomId) {
        return chatGroupRepository.findByChatRoomId(roomId)
                .stream()
                .map(ReadMessageDto::new)
                .toList();
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
                readMessageDto.getLastReadMessageId());
    }

    @Transactional
    public void join(Long roomId, Long userId, List<Long> invitedUserIds) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("room does not exist. room id: " + roomId));

        chatGroupRepository.findByChatRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("The inviter doesn't exist in the chat room." +
                        " room id : " + roomId + " user id : " + userId));

        List<User> users = userRepository.findByIdIn(invitedUserIds);

        if (users.size() != invitedUserIds.size()) {
            throw new IllegalArgumentException("There are users who do not exist.");
        }

        chatRoom.addUsers(users);

        sendMessageToUsers(userId, chatRoom, users);
    }

    @Transactional
    public void createRoom(Long userId, CreateRoomRequest request) {
        ChatRoom chatRoom = ChatRoom.createChatRoom(request.getRoomName());

        chatRoomRepository.save(chatRoom);

        List<User> users = userRepository.findByIdIn(request.getParticipantUserIds());

        if (users.size() != request.getParticipantUserIds().size()) {
            throw new IllegalArgumentException("There are users who do not exist.");
        }

        chatGroupMapper.insertChatGroups(chatRoom.getId(), request.getParticipantUserIds());

        sendMessageToUsers(userId, chatRoom, users);
    }

    private void sendMessageToUsers(Long userId, ChatRoom chatRoom, List<User> users) {
        User inviter = users.stream().filter(user -> user.getId().equals(userId)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Room creator must always be included as a participant."));

        ChatMessage chatMessage = ChatMessage.createMessage(
                createInvitationMessage(inviter.getName(), users.stream().map(User::getName).toList()),
                inviter,
                chatRoom,
                ChatType.JOIN
        );

        chatMessageRepository.save(chatMessage);

        getGroupsByRoomId(chatRoom.getId())
                .forEach(group ->
                        template.convertAndSend("/queue/user/" + group.getUser().getId(),
                                new ChatMessageDto(chatMessage)));
    }

    private String createInvitationMessage(String inviterName, List<String> inviteeNames) {
        return inviterName + "님이 " + String.join(", ", inviteeNames) + "님을 초대했습니다.";
    }
}
