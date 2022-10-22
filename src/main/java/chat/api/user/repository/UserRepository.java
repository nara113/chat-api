package chat.api.user.repository;

import chat.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    @Query("select u from User u left join fetch u.profileImage where u.id = :id")
    Optional<User> findByIdWithProfile(Long id);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByEmail(String email);
}
