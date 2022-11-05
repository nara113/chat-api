package chat.api.room.repository;

import chat.api.room.entity.ChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static chat.api.room.entity.QChatRoom.*;

@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<ChatRoom> findOpenChatRoomByName(String searchText) {
        return query
                .selectFrom(chatRoom)
                .where(chatRoom.name.containsIgnoreCase(searchText)
                        .and(chatRoom.isOpenChatRoom.isTrue()))
                .fetch();
    }

    @Override
    public Optional<ChatRoom> findOpenChatRoomById(Long chatRoomId) {
        return Optional.ofNullable(
                query
                .selectFrom(chatRoom)
                .where(chatRoom.id.eq(chatRoomId)
                        .and(chatRoom.isOpenChatRoom.isTrue()))
                .fetchOne());
    }
}
