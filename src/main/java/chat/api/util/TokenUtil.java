package chat.api.util;

import chat.api.model.TokenConst;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenUtil {

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isNotEmpty(bearerToken) && StringUtils.startsWith(bearerToken, TokenConst.BEARER_TOKEN_PREFIX)) {
            return bearerToken.substring(TokenConst.BEARER_TOKEN_PREFIX.length());
        }

        return null;
    }
}
