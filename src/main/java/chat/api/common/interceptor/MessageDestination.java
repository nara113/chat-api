package chat.api.common.interceptor;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public enum MessageDestination {
    USER("/queue/user/"),
    ROOM("/topic/room/");

    private final String prefix;

    private final Pattern pattern;

    MessageDestination(String prefix) {
        this.prefix = prefix;
        this.pattern = Pattern.compile(prefix + "([0-9]+)");
    }

    public static MessageDestination findByPrefix(String destination) {
        return Arrays.stream(values())
                .filter(dest -> StringUtils.startsWith(destination, dest.getPrefix()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("destination is invalid."));
    }

    public Optional<Long> getId(String destination) {
        Matcher matcher = pattern.matcher(destination);

        if (matcher.find()) {
            return Optional.of(Long.valueOf(matcher.group(1)));
        }

        return Optional.empty();
    }
}
