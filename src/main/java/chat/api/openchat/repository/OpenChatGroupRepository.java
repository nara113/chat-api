package chat.api.openchat.repository;

import chat.api.openchat.entity.OpenChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenChatGroupRepository extends JpaRepository<OpenChatGroup, Long> {
}
