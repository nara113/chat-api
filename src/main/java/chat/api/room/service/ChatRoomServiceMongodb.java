package chat.api.room.service;

import chat.api.message.entity.Message;
import chat.api.message.dto.ChatMessageDto;
import chat.api.message.dto.ChatType;
import chat.api.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ChatRoomServiceMongodb {
    private final MessageRepository messageRepository;

    public String saveChatMessage(ChatMessageDto messageDto) {
        Message message = Message.createMessage(
                messageDto.getMessage(),
                messageDto.getSenderId(),
                messageDto.getRoomId(),
                ChatType.TALK);

        messageRepository.save(message);

        return message.getId();
    }

    public void getMessages(Long roomId, LocalDateTime lastTimestamp) {
        List<Message> messages;

        if (lastTimestamp == null) {
            messages = messageRepository.findTop30ByRoomIdOrderByTimestampDesc(roomId);
        } else {
            messages = messageRepository.findTop30ByRoomIdAndTimestampBeforeOrderByTimestampDesc(roomId, lastTimestamp);
        }
    }

}
