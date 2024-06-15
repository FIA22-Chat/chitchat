package io.github.chitchat.common.filter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.chitchat.common.storage.database.models.User;
import java.time.Duration;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpamFilter {
    private static final int MAX_REQUESTS_USER = 2;
    private static final Duration TIME_WINDOW_USER = Duration.ofSeconds(1);
    private final LoadingCache<User, Integer> rateLimitCache;

    public SpamFilter() {
        rateLimitCache = Caffeine.newBuilder().expireAfterWrite(TIME_WINDOW_USER).build(_ -> 0);
    }

    /**
     * Check if the user is spamming. Calling this method will increment the internal request count
     * for the user.
     *
     * @param user the user to check
     * @return true if the user is spamming, false otherwise
     */
    public boolean isSpamming(User user) {
        int currentCount = rateLimitCache.get(user, _ -> 0);

        if (currentCount >= MAX_REQUESTS_USER) {
            log.trace("User {} is spamming", user);
            return true;
        }

        rateLimitCache.put(user, currentCount + 1);
        return false;
    }
}
