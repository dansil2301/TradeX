package controller.Utils;

import Eco.TradeX.controller.Utils.ErrorResponse;
import Eco.TradeX.controller.Utils.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void testHandleException() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        Exception testException = new Exception("Test Exception Message");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleException(testException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertEquals("Test Exception Message", responseEntity.getBody().getMessage());
    }
}