package chat.api.openchat.repository;

import chat.api.openchat.entity.OpenChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpenChatRoomRepository extends JpaRepository<OpenChatRoom, Long> {
    List<OpenChatRoom> findByNameContainsIgnoreCase(String name);
}
