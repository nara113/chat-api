package chat.api.friend.repository;

import chat.api.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static chat.api.friend.entity.QChatFriend.chatFriend;
import static chat.api.profile.entity.QUploadFile.uploadFile;

@RequiredArgsConstructor
public class ChatFriendRepositoryCustomImpl implements ChatFriendRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<User> selectFriendsByUserId(Long userId) {
        return query
                .select(chatFriend.friend)
                .from(chatFriend)
                .leftJoin(chatFriend.friend.profileImage, uploadFile)
                .fetchJoin()
                .where(chatFriend.user.id.eq(userId)
                        .and(chatFriend.isBlocked.isFalse()))
                .fetch();
    }
}
