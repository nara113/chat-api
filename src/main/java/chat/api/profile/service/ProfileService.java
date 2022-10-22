package chat.api.profile.service;

import chat.api.aws.AwsS3Uploader;
import chat.api.profile.entity.UploadFile;
import chat.api.user.entity.User;
import chat.api.profile.repository.UploadFileRepository;
import chat.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final UploadFileRepository uploadFileRepository;

    private final UserRepository userRepository;

    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public String upload(Long userId,
                         InputStream inputStream,
                         String originalFilename,
                         long size,
                         String contentType) throws IOException {
        User user = userRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new IllegalArgumentException("user does not exist. user id : " + userId));

        String objectUrl = awsS3Uploader.upload(inputStream, originalFilename, contentType);

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(originalFilename)
                .url(objectUrl)
                .size(size)
                .contentType(contentType)
                .user(user)
                .build();

        uploadFileRepository.save(uploadFile);

        user.changeProfileImage(uploadFile);

        return objectUrl;
    }
}
