package Eco.TradeX.business.utils.TraderUtils;

import Eco.TradeX.business.exceptions.InvalidCredentialsException;

public class PasswordChecker {
    public static boolean isPasswordLongEnough(String password) {
        return password.length() >= 10;
    }

    public static boolean isPasswordStrong(String password) {
        if (password.length() >= 80) {
            throw new InvalidCredentialsException("Password should be less than 80 symbols");
        }
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasNumbers = password.matches(".*[0-9].*");
        boolean hasSpecialSymbols = password.matches(".*[^A-Za-z0-9].*");

        return hasUpperCase && hasLowerCase && hasNumbers && hasSpecialSymbols;
    }
}
