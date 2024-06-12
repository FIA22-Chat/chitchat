package io.github.chitchat.common.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@SuppressWarnings("UnstableApiUsage")
public class ProfanityFilter {
    private final BloomFilter<String> profanityBloomFilter;
    private final Set<String> profanityList;

    /**
     * Create a new profanity filter with the given list of profanities.
     *
     * @param profanities the list of profanities
     */
    public ProfanityFilter(@NotNull List<String> profanities) {
        var initialCapacity = profanities.size();
        log.trace("Creating profanity filters with initial capacity {}", initialCapacity);

        this.profanityBloomFilter =
                BloomFilter.create(
                        Funnels.stringFunnel(StandardCharsets.UTF_8), initialCapacity, 0.01);
        this.profanityList = Collections.synchronizedSet(new HashSet<>(initialCapacity));

        profanities.forEach(
                profanity -> {
                    profanityBloomFilter.put(profanity);
                    profanityList.add(profanity);
                });
    }

    /**
     * Check if any of the words in the message contains profanities where space splits words.
     *
     * @param message the message to check
     * @return true if the message contains profanities, false otherwise
     * @see ProfanityFilter#withCensor(String)
     */
    public boolean containsProfanities(@NotNull String message) {
        var words = message.split(" ");

        for (var word : words)
            if (profanityBloomFilter.mightContain(word) && profanityList.contains(word))
                return true;

        return false;
    }

    /**
     * Replace any profanities in the message with asterisks where space splits words.
     *
     * @param message the message to censor
     * @return the censored message
     * @see ProfanityFilter#containsProfanities(String)
     */
    public String withCensor(@NotNull String message) {
        var words = message.split(" ");

        var finalMessage = new StringBuilder();
        for (var word : words) {
            if (profanityBloomFilter.mightContain(word) && profanityList.contains(word)) {
                log.trace("Censoring profanity {}", word);

                var size = word.length();
                var replacement = "*".repeat(size);
                word = word.replaceAll(word, replacement);
            }

            finalMessage.append(word).append(" ");
        }

        return finalMessage.toString();
    }
}
