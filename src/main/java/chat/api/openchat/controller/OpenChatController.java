package chat.api.openchat.controller;

import chat.api.common.model.Response;
import chat.api.openchat.dto.request.CreateOpenChatRequest;
import chat.api.openchat.service.OpenChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/open-chat")
@RestController
public class OpenChatController {
    private final OpenChatService openChatService;

    @PostMapping
    public Response<Long> createOpenGroupChatRoom(@Valid @RequestBody CreateOpenChatRequest request) {
        return Response.of(openChatService.createOpenChatRoom(request));
    }

    @GetMapping
    public Response searchOpenChat(String searchText) {
        return Response.of(openChatService.searchOpenChat(searchText));
    }
}
