package chat.api.util;

import chat.api.model.TokenConst;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenUtil {

    public static String resolveToken(String bearerToken) {
        if (StringUtils.isNotEmpty(bearerToken)
                && StringUtils.startsWith(bearerToken, TokenConst.BEARER_TOKEN_PREFIX)) {
            return bearerToken.substring(TokenConst.BEARER_TOKEN_PREFIX.length());
        }

        return null;
    }
}
