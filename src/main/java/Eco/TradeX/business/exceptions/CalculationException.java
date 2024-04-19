package Eco.TradeX.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CalculationException extends ResponseStatusException {
    public CalculationException(String errorMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Calculator Error: " + errorMessage);
    }
}
