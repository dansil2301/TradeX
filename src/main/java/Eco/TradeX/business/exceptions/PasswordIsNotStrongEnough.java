package Eco.TradeX.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordIsNotStrongEnough extends ResponseStatusException {
    public PasswordIsNotStrongEnough(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, "Trader Error: " + errorMessage);
    }
}
