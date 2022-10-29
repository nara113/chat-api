package chat.api.room.controller;

import chat.api.common.argumentresolver.User;
import chat.api.common.model.Response;
import chat.api.message.dto.ChatMessageDto;
import chat.api.message.dto.ReadMessageDto;
import chat.api.room.dto.request.CreateRoomRequest;
import chat.api.room.dto.ChatRoomDto;
import chat.api.room.service.ChatRoomService;
import chat.api.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/rooms")
@RestController
public class RoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 목록")
    @GetMapping
    public Response<List<ChatRoomDto>> getRooms(@Parameter(hidden = true) @User UserDto user) {
        return Response.of(chatRoomService.getAllRoom(user.getUserId()));
    }

    @Operation(summary = "채팅방 정보")
    @GetMapping("/{roomId}")
    public Response<ChatRoomDto> getRoom(@Parameter(hidden = true) @User UserDto user,
                                         @PathVariable Long roomId) {
        return Response.of(chatRoomService.getRoom(user.getUserId(), roomId));
    }

    @Operation(summary = "채팅방 생성")
    @PostMapping
    public Response<String> createRoom(
            @Parameter(hidden = true) @User UserDto user,
            @Valid @RequestBody CreateRoomRequest createRoomRequest) {
        chatRoomService.createRoom(user.getUserId(), createRoomRequest);
        return Response.of("ok");
    }

    @Operation(summary = "채팅방 메시지 목록")
    @GetMapping("/{roomId}/messages")
    public Response<List<ChatMessageDto>> getMessages(@PathVariable Long roomId,
                                                      @RequestParam(required = false) Long lastMessageId) {
        return Response.of(chatRoomService.getMessages(roomId, lastMessageId));
    }

    @Operation(summary = "채팅방 유저별 마지막 읽은 메시지 아이디 목록")
    @GetMapping("/{roomId}/last-read")
    public Response<List<ReadMessageDto>> getUsersByRoom(@PathVariable Long roomId) {
        return Response.of(chatRoomService.getLastReadMessagesByRoomId(roomId));
    }

    @Operation(summary = "채팅방 대화상대 추가")
    @PostMapping("/{roomId}/users")
    public Response<String> addUsers(@Parameter(hidden = true) @User UserDto user,
                                     @PathVariable Long roomId,
                                     @RequestBody List<Long> invitedUserIds) {
        chatRoomService.join(roomId, user.getUserId(), invitedUserIds);
        return Response.of("ok");
    }

    @Operation(summary = "채팅방 퇴장")
    @DeleteMapping("/{roomId}/user")
    public Response deleteUser(@Parameter(hidden = true) @User UserDto user,
                               @PathVariable String roomId) {

        return null;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    private Response<String> violationException() {
        return Response.of(HttpStatus.CONFLICT.value(), "User already exists in chat room.");
    }
}
