package com.banking.digitalbankingproject.tools;

import org.mindrot.jbcrypt.BCrypt;

public class Utils {
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
