package chat.api.validator;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {
    private final List<String> imageContentTypes = List.of("image/jpeg", "image/jpg", "image/png");

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        if (multipartFile == null) {
            return false;
        }

        boolean result = true;

        String contentType = multipartFile.getContentType();

        if (!isSupportedContentType(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only PNG or JPG images are allowed.")
                    .addConstraintViolation();

            result = false;
        }

        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return CollectionUtils.containsAny(imageContentTypes, contentType);
    }
}