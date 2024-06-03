package io.github.chitchat.auth;

import java.security.SecureRandom;
import java.util.Base64;

public class SaltGenerator
{

    public static String generateSalt(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[length];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
