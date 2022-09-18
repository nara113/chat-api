package chat.api.service;

import chat.api.aws.AwsS3Uploader;
import chat.api.entity.*;
import chat.api.mapper.ChatGroupMapper;
import chat.api.mapper.ChatMapper;
import chat.api.model.*;
import chat.api.model.request.CreateRoomRequest;
import chat.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.stream.Collectors.*;

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

    private final ChatGroupMapper chatGroupMapper;

    private final AwsS3Uploader awsS3Uploader;

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

    public List<ChatMessageDto> getMessages(Long roomId, Long messageId) {
        List<ChatMessage> messages;

        if (messageId == null) {
            messages = chatMessageRepository.findTop30ByChatRoomIdOrderByIdDesc(roomId);
        } else {
            messages = chatMessageRepository.findTop30ByChatRoomIdAndIdIsLessThanOrderByIdDesc(roomId, messageId);
        }

        return messages.stream()
                .map(ChatMessageDto::new)
                .collect(toList());
    }

    public List<UserDto> getFriends(Long userId) {
        return chatFriendRepository.findFriendByUserIdAndBlockYn(userId, "N")
                .stream()
                .map(UserDto::new)
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

        chatGroup.setLastReadMessageId(lastMessageId);

        return lastMessageId;
    }

    public void markAsRead(ReadMessageDto readMessageDto) {
        chatGroupRepository.updateLastReadMessageId(
                readMessageDto.getRoomId(),
                readMessageDto.getUserId(),
                readMessageDto.getMessageId());
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

    @Transactional
    public String upload(Long userId, InputStream inputStream, String originalFilename, String contentType) throws IOException {
        String objectUrl = awsS3Uploader.upload(inputStream, originalFilename, contentType);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user does not exist. user id : " + userId));

        user.changeProfileImageUrl(objectUrl);

        return objectUrl;
    }
}
