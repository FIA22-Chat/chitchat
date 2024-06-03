package io.github.chitchat.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashSHA256
{
    private String hash = null;

    private byte[] hashBytes = null;
    private MessageDigest digest;
    private StringBuilder hexString = new StringBuilder();
    private String hex = null;

    public HashSHA256(String input)
    {
        this.hash = getHash(input);
    }

    public String getHash(String input)
    {
        try
        {
            digest = MessageDigest.getInstance("SHA-256"); // Instance SHA-256-hash-algorithm
            hashBytes = digest.digest(input.getBytes());// Convert Input into byte-array

            for (byte b : hashBytes) // Convert Byte-Array into hexadecimal string
            {
                hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1)
                {
                    hexString.append('0');
                }

                hexString.append(hex);
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return hexString.toString();
    }

    public String toString()
    {
        return hash;
    }
}
