package chat.api.repository;

import chat.api.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static chat.api.entity.QChatFriend.chatFriend;
import static chat.api.entity.QUploadFile.uploadFile;

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
                        .and(chatFriend.blockYn.eq("N")))
                .fetch();
    }
}
