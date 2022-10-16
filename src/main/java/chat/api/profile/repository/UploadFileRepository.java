package chat.api.profile.repository;

import chat.api.profile.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
