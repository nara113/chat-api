package chat.api.controller;

import chat.api.argumentresolver.User;
import chat.api.model.Response;
import chat.api.model.UserDto;
import chat.api.service.ChatService;
import chat.api.validator.ValidImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProfileController {

    private final ChatService chatService;

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
