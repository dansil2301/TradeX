package Eco.TradeX.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TraderAlreadyExistsException extends ResponseStatusException {
    public TraderAlreadyExistsException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, "Trader Error: " + errorMessage);
    }
}
