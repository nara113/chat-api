package chat.api.service;

import chat.api.aws.AwsS3Uploader;
import chat.api.entity.*;
import chat.api.mapper.ChatGroupMapper;
import chat.api.mapper.ChatMapper;
import chat.api.model.*;
import chat.api.model.request.CreateRoomRequest;
import chat.api.repository.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static chat.api.entity.QChatFriend.*;
import static chat.api.entity.QUploadFile.*;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
public class ChatService {
    private final ChatMapper chatMapper;

    private final ChatMessageRepository chatMessageRepository;

    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatGroupRepository chatGroupRepository;

    private final UploadFileRepository uploadFileRepository;

    private final ChatGroupMapper chatGroupMapper;

    private final AwsS3Uploader awsS3Uploader;

    private final JPAQueryFactory query;

    public ChatService(ChatMapper chatMapper,
                       ChatMessageRepository chatMessageRepository,
                       UserRepository userRepository,
                       ChatRoomRepository chatRoomRepository,
                       ChatGroupRepository chatGroupRepository,
                       UploadFileRepository uploadFileRepository,
                       ChatGroupMapper chatGroupMapper,
                       AwsS3Uploader awsS3Uploader,
                       EntityManager em) {
        this.chatMapper = chatMapper;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.uploadFileRepository = uploadFileRepository;
        this.chatGroupMapper = chatGroupMapper;
        this.awsS3Uploader = awsS3Uploader;
        this.query = new JPAQueryFactory(em);
    }

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
        List<User> friends = query
                .select(chatFriend.friend)
                .from(chatFriend)
                .leftJoin(chatFriend.friend.profileImage, uploadFile).fetchJoin()
                .where(chatFriend.user.id.eq(userId).and(chatFriend.blockYn.eq("N")))
                .fetch();

        return friends
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
    public String upload(Long userId,
                         InputStream inputStream,
                         String originalFilename,
                         long size,
                         String contentType) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user does not exist. user id : " + userId));

        String objectUrl = awsS3Uploader.upload(inputStream, originalFilename, contentType);

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(originalFilename)
                .url(objectUrl)
                .size(size)
                .contentType(contentType)
                .user(user)
                .build();

        uploadFileRepository.save(uploadFile);

        user.changeProfileImage(uploadFile);

        return objectUrl;
    }
}
