package controller.Utils;

import Eco.TradeX.controller.Utils.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {
    @Test
    void testGetMessage() {
        String errorMessage = "Test Error Message";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        assertEquals(errorMessage, errorResponse.getMessage());
    }

    @Test
    void testSetMessage() {
        String errorMessage = "Test Error Message";
        ErrorResponse errorResponse = new ErrorResponse("");
        errorResponse.setMessage(errorMessage);
        assertEquals(errorMessage, errorResponse.getMessage());
    }
}