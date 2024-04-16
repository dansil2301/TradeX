package Eco.TradeX.business.utils.TraderUtils;

public class PasswordChecker {
    public static boolean isPasswordLongEnough(String password) {
        return password.length() >= 10;
    }

    public static boolean isPasswordStrong(String password) {
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasNumbers = password.matches(".*[0-9].*");
        boolean hasSpecialSymbols = password.matches(".*[^A-Za-z0-9].*");

        return hasUpperCase && hasLowerCase && hasNumbers && hasSpecialSymbols;
    }
}
