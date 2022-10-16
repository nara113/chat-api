package chat.api.user.repository;

import chat.api.user.entity.User;

public interface UserRepositoryCustom {
    User findUserByEmailWithProfile(String email);
}
