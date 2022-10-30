package chat.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniqueIdGenerator {

    public static String generateStringId() {
        return new Date().getTime() + UUID.randomUUID().toString().substring(0, 8);
    }
}
