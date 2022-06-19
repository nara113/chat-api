package chat.api.mapper;

import chat.api.model.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    List<ChatRoomDto> selectAllRoomsByUserId(Long userId);
}
