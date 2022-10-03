package chat.api.controller;

import chat.api.argumentresolver.User;
import chat.api.model.*;
import chat.api.model.request.CreateRoomRequest;
import chat.api.service.ChatService;
import chat.api.validator.ValidImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅방 목록")
    @GetMapping("/rooms")
    public Response<List<ChatRoomDto>> getRooms(@Parameter(hidden = true) @User UserDto user) {
        return Response.of(chatService.getAllRoom(user.getUserId()));
    }

    @Operation(summary = "채팅방 정보")
    @GetMapping("/rooms/{roomId}")
    public Response<ChatRoomDto> getRoom(@Parameter(hidden = true) @User UserDto user,
                                               @PathVariable Long roomId) {
        return Response.of(chatService.getRoom(user.getUserId(), roomId));
    }

    @Operation(summary = "채팅방 메시지 목록")
    @GetMapping("/rooms/{roomId}/messages")
    public Response<List<ChatMessageDto>> getMessages(@PathVariable Long roomId,
                                                      @RequestParam(required = false) Long messageId) {
        return Response.of(chatService.getMessages(roomId, messageId));
    }

    @Operation(summary = "채팅방 유저별 마지막 읽은 메시지 아이디 목록")
    @GetMapping("/rooms/{roomId}/last-read")
    public Response<List<LastReadMessageDto>> getUsersByRoom(@PathVariable Long roomId) {
        return Response.of(chatService.getLastReadMessagesByRoomId(roomId));
    }

    @Operation(summary = "친구 목록")
    @GetMapping("/friends")
    public Response<List<UserDto>> getFriends(@Parameter(hidden = true) @User UserDto user) {
        return Response.of(chatService.getFriends(user.getUserId()));
    }

    @Operation(summary = "채팅방 입장")
    @PostMapping("/rooms/{roomId}/user")
    public Response addUser(@AuthenticationPrincipal UserDetails user2,
                            @Parameter(hidden = true) @User UserDto user,
                            @PathVariable Long roomId) {
        chatService.enterRoom(roomId, user.getUserId());
        return null;
    }

    @Operation(summary = "채팅방 퇴장")
    @DeleteMapping("/rooms/{roomId}/user")
    public Response deleteUser(@Parameter(hidden = true) @User UserDto user,
                               @PathVariable String roomId) {

        return null;
    }

    @Operation(summary = "채팅방 생성")
    @PostMapping("/rooms")
    public Response<String> createRoom(
            @Parameter(hidden = true) @User UserDto user,
            @Valid @RequestBody CreateRoomRequest createRoomRequest) {
        chatService.createRoom(user.getUserId(), createRoomRequest);
        return Response.of("ok");
    }

    @Operation(summary = "프로필 이미지 업로드")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/upload/profile-image")
    public Response<String> uploadFile(
            @Parameter(hidden = true) @User UserDto user,
            @ValidImage @RequestParam("image") MultipartFile multipartFile) throws IOException {
        return Response.of(
                HttpStatus.CREATED.value(),
                chatService.upload(
                        user.getUserId(),
                        multipartFile.getInputStream(),
                        multipartFile.getOriginalFilename(),
                        multipartFile.getSize(),
                        multipartFile.getContentType())
        );
    }
}
