package io.github.chitchat.auth;

import io.github.chitchat.storage.local.LocalStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import static io.github.chitchat.auth.SaltGenerator.generateSalt;

public class Authentication
{
    private static final Logger log = LogManager.getLogger(Authentication.class);
    private static Argon2PasswordEncoder hashSalt = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);


    public Authentication()
    {
        getSignUp(new User("Azlack", "azl@googlemail.com"), "1234", "1234");
    }

    public static boolean getSignUp(@NotNull User user, @NotNull String password1, @NotNull String password2)
    {
        // Check if userNAME exists in database
        //if()

        if(!(password1.matches(password2)))
        {
            System.out.println("Password is not matching");
            return false;
        }

        String springBouncyHash = hashSalt.encode(password2);

        System.out.println(springBouncyHash);
        log.debug(springBouncyHash);

        /*
        String salt = generateSalt(16);
        // Salt must be saved in the database

        HashSHA256 hashSaltPwd = new HashSHA256(password1 + salt);
        // hashSaltPwd must be saved in the database

        // Username must be saved in the database
        */
        return true;
    }

    public static boolean getSignIn(@NotNull String username, @NotNull String useremail, @NotNull String password)
    {
        int failedAttempts = 0;
        final int MAX_ATTEMPTS = 3;
        final int DELAY_SECONDS = 10; // against dos
        boolean loginSuccess = false;

        // Checks if user already exists
        if (!(user.userExists( )))
        {
            System.out.println("User already exists");
        }

        do
        {
            System.out.println("Enter your username: ");

            user.setUsername(user.getUserInput("username"));

            // Check if user exists
            if (!(user.userExists()))
            {
                System.out.println("User already exists");
            }

        }
        while (!(userExists(uname, uname)));


        System.out.println("Enter your password: ");
        user.setUsername(user.getUserInput("password"));

        loginSuccess = user.loginUser();


        try // Prevents BruteForce-Attacks
        {
            if (!loginSuccess)
            {
                failedAttempts++;

                if (failedAttempts >= MAX_ATTEMPTS) // checks amount of failed attempts
                {
                    System.out.println("\nToo many failed attempts. Please wait for " + DELAY_SECONDS + " seconds before trying again.");
                    Thread.sleep(DELAY_SECONDS * 1000); // Waits 10 sec
                    failedAttempts = 0;// Reset variable
                }
                else
                {
                    System.out.println("\nThe username or password is incorrect! Please try this again.");
                }
            }
            else
            {
                pt.printNewPage();
            }
        }
        catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }

        return true;
    }



    public static boolean userExists(String name, String email)
    {
        //boolean exist = true;

        // search in database for username and email
        // exist = false;

        //return exist;
        return false;
    }

    // ACTUAL PROGRAM
    // ###################################################################################################
    /*
    public static void access()
    {
        // Access to the actual program
    }
    */
}
