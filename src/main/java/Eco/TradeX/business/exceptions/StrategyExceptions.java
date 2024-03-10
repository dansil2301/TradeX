package Eco.TradeX.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StrategyExceptions extends ResponseStatusException {
    public StrategyExceptions(String errorMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Strategy Error: " + errorMessage);
    }
}
