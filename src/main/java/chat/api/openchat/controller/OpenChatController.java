package chat.api.openchat.controller;

import chat.api.common.argumentresolver.RequestUser;
import chat.api.common.model.Response;
import chat.api.openchat.dto.OpenChatRoomDto;
import chat.api.openchat.dto.request.CreateOpenChatRequest;
import chat.api.openchat.service.OpenChatService;
import chat.api.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/open-chat")
@RestController
public class OpenChatController {
    private final OpenChatService openChatService;

    @Operation(summary = "오픈 채팅방 생성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Response<Long> createOpenGroupChatRoom(@Valid @RequestBody CreateOpenChatRequest openChatRequest,
                                                  @RequestUser User user) {
        return Response.of(HttpStatus.CREATED.value(), openChatService.createOpenChatRoom(openChatRequest, user));
    }

    @Operation(summary = "오픈 채팅방 검색")
    @GetMapping
    public Response<List<OpenChatRoomDto>> searchOpenChat(String searchText) {
        return Response.of(openChatService.searchOpenChatRooms(searchText));
    }
}
