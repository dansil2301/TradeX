package controller;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.controller.CandlesController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CandlesControllerTest {
    private MockMvc mockMvc;

    @Mock
    private GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CandlesController candlesController = new CandlesController(getCandlesAPIInformationUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(candlesController).build();
    }

    @Test
    void testGetCandles() throws Exception {
        String fromLocal = "2023-01-01T00:00:00Z";
        String toLocal = "2023-01-02T00:00:00Z";

        Instant from = Instant.parse("2023-01-01T00:00:00Z");
        Instant to = Instant.parse("2023-01-02T00:00:00Z");

        String figi = "FIGI123";
        CandleInterval interval = CandleInterval.CANDLE_INTERVAL_DAY;

        when(getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(from, to, figi, interval))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/candles")
                        .param("from", fromLocal)
                        .param("to", toLocal)
                        .param("figi", figi)
                        .param("interval", interval.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.to").value(to.toEpochMilli() / 1000))
                .andExpect(jsonPath("$.from").value(from.toEpochMilli() / 1000))
                .andExpect(jsonPath("$.figi").value(figi))
                .andExpect(jsonPath("$.interval").value(interval.toString()))
                .andExpect(jsonPath("$.candles").isArray())
                .andExpect(jsonPath("$.candles").isEmpty());

        verify(getCandlesAPIInformationUseCase, times(1)).getHistoricalCandlesAPI(from, to, figi, interval);
    }
}