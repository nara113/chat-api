package chat.api.room.repository.mapper;

import chat.api.room.dto.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RoomMapper {
    List<ChatRoomDto> selectAllRoomsByUserId(Long userId);

    Optional<ChatRoomDto> selectRoomByUserIdAndRoomId(Long userId, Long roomId);
}
