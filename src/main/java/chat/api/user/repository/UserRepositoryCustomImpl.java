package chat.api.user.repository;

import chat.api.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static chat.api.profile.entity.QUploadFile.uploadFile;
import static chat.api.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public User findUserByEmailWithProfile(String email) {
        return query
                .selectFrom(user)
                .leftJoin(user.profileImage, uploadFile).fetchJoin()
                .where(user.email.eq(email))
                .fetchOne();
    }
}
