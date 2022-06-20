package chat.api.chat.mapper;

import chat.api.chat.model.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    List<ChatRoomDto> selectAllRoomsByUserId(Long userId);
}
