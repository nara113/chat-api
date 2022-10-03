package chat.api.mapper;

import chat.api.model.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RoomMapper {
    List<ChatRoomDto> selectAllRoomsByUserId(Long userId);

    Optional<ChatRoomDto> selectRoomByUserIdAndRoomId(Long userId, Long roomId);
}
