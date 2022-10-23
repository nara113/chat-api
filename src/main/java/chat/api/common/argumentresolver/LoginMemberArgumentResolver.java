package chat.api.common.argumentresolver;

import chat.api.user.dto.UserDto;
import chat.api.user.service.UserService;
import chat.api.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(User.class);
        boolean hasMemberType = UserDto.class.isAssignableFrom(parameter.getParameterType());

        return hasParameterAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String currentUserEmail = SecurityUtil.getCurrentUserEmail()
                .orElseThrow(() -> new IllegalStateException("current user id does not exist."));

        return new UserDto(userService.getUserByEmail(currentUserEmail));
    }
}
