package chat.api.room.service;

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
import org.apache.commons.lang3.StringUtils;
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

        ChatMessage chatMessage = ChatMessage.createTalkMessage(
                messageDto.getMessage(),
                user,
                chatRoom
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
    public void join(Long roomId, User inviter, List<Long> invitedUserIds) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("room does not exist. room id: " + roomId));

        chatGroupRepository.findByChatRoomIdAndUserId(roomId, inviter.getId())
                .orElseThrow(() -> new IllegalArgumentException("The inviter doesn't exist in the chat room." +
                        " room id : " + roomId + " inviter id : " + inviter.getId()));

        List<User> users = userRepository.findByIdIn(invitedUserIds);

        if (users.size() != invitedUserIds.size()) {
            throw new IllegalArgumentException("There are users who do not exist.");
        }

        List<Long> participantIds = users.stream().map(User::getId).toList();
        chatGroupMapper.insertChatGroups(chatRoom.getId(), participantIds);

        final String invitationMessage = createInvitationMessage(
                inviter.getName(),
                users.stream().map(User::getName).toList());

        ChatMessage chatMessage = ChatMessage.createJoinMessage(
                invitationMessage,
                inviter,
                chatRoom
        );

        chatMessageRepository.save(chatMessage);

        final List<Long> userIds = getGroupsByRoomId(chatRoom.getId())
                .stream().map(ChatGroup::getUser).map(User::getId).toList();

        sendMessageToUsers(userIds, chatMessage);
    }

    @Transactional
    public void createRoom(User creator, CreateRoomRequest request) {
        ChatRoom chatRoom = ChatRoom.createChatRoom(request.getRoomName(), request.getParticipantUserIds().size());

        chatRoomRepository.save(chatRoom);

        List<User> users = userRepository.findByIdIn(request.getParticipantUserIds());

        if (users.size() != request.getParticipantUserIds().size()) {
            throw new IllegalArgumentException("There are users who do not exist.");
        }

        chatGroupMapper.insertChatGroups(chatRoom.getId(), request.getParticipantUserIds());

        final String invitationMessage = createInvitationMessage(
                creator.getName(),
                users.stream()
                        .filter(user -> !user.getId().equals(creator.getId()))
                        .map(User::getName)
                        .toList());

        ChatMessage chatMessage = ChatMessage.createJoinMessage(
                invitationMessage,
                creator,
                chatRoom
        );

        chatMessageRepository.save(chatMessage);

        sendMessageToUsers(request.getParticipantUserIds(), chatMessage);
    }

    private void sendMessageToUsers(List<Long> userIds, ChatMessage chatMessage) {
        userIds.forEach(userId ->
                template.convertAndSend("/queue/user/" + userId, new ChatMessageDto(chatMessage)));
    }

    private String createInvitationMessage(String inviterName, List<String> inviteeNames) {
        return inviterName + "님이 " + StringUtils.join(inviteeNames, ", ") + "님을 초대했습니다.";
    }
}
