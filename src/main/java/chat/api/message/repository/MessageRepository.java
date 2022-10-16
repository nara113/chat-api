package chat.api.message.repository;

import chat.api.message.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findTop30ByRoomIdOrderByTimestampDesc(Long roomId);

    List<Message> findTop30ByRoomIdAndTimestampBeforeOrderByTimestampDesc(Long roomId, LocalDateTime timestamp);

    Optional<Message> findFirstByRoomIdOrderByTimestampDesc(Long roomId);

    Long countByRoomIdAndTimestampAfter(Long roomId, LocalDateTime timestamp);
}
