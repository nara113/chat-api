package chat.api.user.repository;

import chat.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    @Query("select u.id from User u where u.email = :email")
    Long findUserIdByEmail(String email);
}
