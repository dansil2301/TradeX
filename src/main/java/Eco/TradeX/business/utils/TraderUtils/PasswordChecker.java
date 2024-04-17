package Eco.TradeX.business.utils.TraderUtils;

import Eco.TradeX.business.exceptions.InvalidCredentialsException;

import java.util.regex.Pattern;

public class PasswordChecker {
    public static boolean isPasswordLongEnough(String password) {
        return password.length() >= 10;
    }

    public static boolean isPasswordStrong(String password) {
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern numberPattern = Pattern.compile("[0-9]");
        Pattern specialSymbolPattern = Pattern.compile("[^A-Za-z0-9]");

        boolean hasUpperCase = upperCasePattern.matcher(password).find();
        boolean hasLowerCase = lowerCasePattern.matcher(password).find();
        boolean hasNumbers = numberPattern.matcher(password).find();
        boolean hasSpecialSymbols = specialSymbolPattern.matcher(password).find();

        return hasUpperCase && hasLowerCase && hasNumbers && hasSpecialSymbols;
    }
}
