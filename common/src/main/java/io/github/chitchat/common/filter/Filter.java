package io.github.chitchat.common.filter;

import io.github.chitchat.common.filter.reader.DHio;
import java.util.ArrayList;
import java.util.List;

public class Filter {
    private static ArrayList<String> spamList = new ArrayList<>();
    ProfanityFilter profanityFilter = new ProfanityFilter(1000, 0.01);
    User user;
    DHio reader = new DHio();

    public void fill() {
        spamList.addAll(List.of(reader.lesen()));
        for (String s : spamList) {
            profanityFilter.addMsg(s);
        }
    }

    public void checking(String incomingMessage) {
        if (!profanityFilter.isSpam(incomingMessage)) {
            System.out.println("Passt");
        }
        System.out.println("Spam!!!!!");
        if (user.getFilter()) {
            profanityFilter.censor(incomingMessage);
        }
    }
}
