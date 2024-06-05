package io.github.chitchat.common.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.lang.reflect.Proxy;
import java.util.ArrayList;

public class ProfanityFilter {

    private BloomFilter<CharSequence> bloomFilter;

    public ProfanityFilter(int expectedInsertions, double falsePositiveRate)
    {
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(null), expectedInsertions);
    }
    public void addMsg(String message)
    {
        bloomFilter.put(message);
    }
    public boolean isSpam(String message)
    {
        return bloomFilter.mightContain(message);
    }

    public static String censor(String str)
    {
        if(str.length()<=2)
        {
            return str;
        }
        char firstLetter=str.charAt(0);
        char lastLetter=str.charAt(str.length()-1);
        StringBuilder censor = new StringBuilder();
        for (int i = 1;i<str.length()-1;i++)
        {
            censor.append('*');
        }
        return firstLetter+censor.toString()+lastLetter;
    }
}
