package chat.api.repository;

import chat.api.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static chat.api.entity.QChatFriend.chatFriend;
import static chat.api.entity.QUploadFile.uploadFile;

@Repository
@RequiredArgsConstructor
public class ChatFriendQueryRepository {
    private final JPAQueryFactory query;

    public List<User> selectFriendsByUserId(Long userId) {
        return query
                .select(chatFriend.friend)
                .from(chatFriend)
                .leftJoin(chatFriend.friend.profileImage, uploadFile)
                .fetchJoin()
                .where(chatFriend.user.id.eq(userId)
                        .and(chatFriend.blockYn.eq("N")))
                .fetch();
    }
}
