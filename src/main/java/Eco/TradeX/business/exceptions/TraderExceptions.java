package Eco.TradeX.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TraderExceptions extends ResponseStatusException {
    public TraderExceptions(String errorMessage) {
        super(HttpStatus.NO_CONTENT, "Trader Error: " + errorMessage);
    }
}
