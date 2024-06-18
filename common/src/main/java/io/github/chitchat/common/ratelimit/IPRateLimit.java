package io.github.chitchat.common.ratelimit;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class IPRateLimit {
    private static final int MAX_REQUESTS_IP = 5;
    private static final Duration TIME_WINDOW_IP = Duration.ofSeconds(300);
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(5);

    private final ScheduledExecutorService scheduler;

    private final LoadingCache<InetAddress, Integer> rateLimitCache;
    private final Set<InetAddress> blockedIPs;

    public IPRateLimit() {
        scheduler = Executors.newScheduledThreadPool(1, Thread::startVirtualThread);
        rateLimitCache = Caffeine.newBuilder().expireAfterWrite(TIME_WINDOW_IP).build(_ -> 0);
        blockedIPs = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * Check if the IP address is rate limited. Calling this method will increment the internal
     * request count for the IP address.
     *
     * @param ipAddress the IP address to check
     * @return true if the IP address is rate limited, false otherwise
     */
    public boolean isRateLimited(InetAddress ipAddress) {
        if (blockedIPs.contains(ipAddress)) return true;

        var currentCount = rateLimitCache.get(ipAddress);
        rateLimitCache.put(ipAddress, currentCount + 1);
        if (currentCount >= MAX_REQUESTS_IP) {
            blockIP(ipAddress);
            return true;
        }

        return false;
    }

    /**
     * Block the IP address for duration defined by {@link IPRateLimit#BLOCK_DURATION}.
     *
     * @param ipAddress the IP address to block
     */
    public void blockIP(InetAddress ipAddress) {
        log.warn("Blocking IP address {}", ipAddress);
        blockedIPs.add(ipAddress);

        scheduler.schedule(
                () -> blockedIPs.remove(ipAddress),
                BLOCK_DURATION.toMillis(),
                TimeUnit.MILLISECONDS);
    }
}
