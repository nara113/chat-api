package chat.api.room.repository.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatGroupMapper {
    int insertChatGroups(Long roomId, List<Long> participantUserIds);
}
