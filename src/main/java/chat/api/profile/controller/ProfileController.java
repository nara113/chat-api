package chat.api.profile.controller;

import chat.api.common.argumentresolver.User;
import chat.api.common.model.Response;
import chat.api.user.dto.UserDto;
import chat.api.profile.service.ProfileService;
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

    private final ProfileService profileService;

    @Operation(summary = "프로필 이미지 업로드")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/upload/profile-image")
    public Response<String> uploadFile(
            @Parameter(hidden = true) @User UserDto user,
            @ValidImage @RequestParam("image") MultipartFile multipartFile) throws IOException {
        return Response.of(
                HttpStatus.CREATED.value(),
                profileService.upload(
                        user.getUserId(),
                        multipartFile.getInputStream(),
                        multipartFile.getOriginalFilename(),
                        multipartFile.getSize(),
                        multipartFile.getContentType())
        );
    }
}
