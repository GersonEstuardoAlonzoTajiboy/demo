package com.example.demo.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Utility for secure password management using BCrypt.
 */
public class SecurityUtil {

    // Number of rounds for the BCrypt algorithm
    private static final int COST = 12;

    private SecurityUtil() {
        throw new UnsupportedOperationException("Utility class. Do not instantiate!");
    }

    /**
     * Generates a secure hash for the given password.
     *
     * @param password The password in clear text.
     * @return The secure hash of the password.
     */
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(COST, password.toCharArray());
    }

    /**
     * Verifies that the clear text password matches the hash.
     *
     * @param password       Plain-text password.
     * @param hashedPassword Hashed password stored.
     * @return true if the password is valid, false otherwise.
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}
