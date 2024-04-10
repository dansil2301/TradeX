package controller;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyFactoryUseCase;
import Eco.TradeX.controller.StrategiesController;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StrategiesControllerTest {
    @Mock
    private StrategyFactoryUseCase strategyFactoryUseCase;

    @Mock
    private GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;

    @InjectMocks
    private StrategiesController strategiesController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(strategiesController).build();
    }


    @Test
    void testGetStrategyNames() throws Exception {
        when(strategyFactoryUseCase.getStrategiesNames())
                .thenReturn(Arrays.asList("Strategy1", "Strategy2"));

        mockMvc.perform(get("/api/strategies/get-strategies-names")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.strategyNames").isArray())
                .andExpect(jsonPath("$.strategyNames.length()").value(2))
                .andExpect(jsonPath("$.strategyNames[0]").value("Strategy1"))
                .andExpect(jsonPath("$.strategyNames[1]").value("Strategy2"));

        verify(strategyFactoryUseCase, times(1)).getStrategiesNames();
        verifyNoMoreInteractions(strategyFactoryUseCase);
    }

    @Test
    void testGetPeriodStrategyParams() throws Exception {
        String fromLocal = "2023-01-01T00:00:00Z";
        String toLocal = "2023-01-02T00:00:00Z";

        when(getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<CandleStrategiesParams> mockParams = Arrays.asList(CandleStrategiesParams.builder().build(), CandleStrategiesParams.builder().build());
        when(strategyFactoryUseCase.getCandlesStrategiesParameters(anyList(), anyList(), any(), any(), any()))
                .thenReturn(mockParams);

        mockMvc.perform(get("/api/strategies/get-strategy-params-without-candles")
                        .param("from", fromLocal)
                        .param("to", toLocal)
                        .param("figi", "FIGI123")
                        .param("interval", CandleInterval.CANDLE_INTERVAL_DAY.toString())
                        .param("strategiesNames", "Strategy1", "Strategy2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.to").exists())
                .andExpect(jsonPath("$.from").exists())
                .andExpect(jsonPath("$.figi").value("FIGI123"))
                .andExpect(jsonPath("$.interval").value(CandleInterval.CANDLE_INTERVAL_DAY.toString()))
                .andExpect(jsonPath("$.strategiesParams").isArray())
                .andExpect(jsonPath("$.strategiesParams.length()").value(2));

        verify(getCandlesAPIInformationUseCase, times(1)).getHistoricalCandlesAPI(any(), any(), any(), any());
        verify(strategyFactoryUseCase, times(1)).getCandlesStrategiesParameters(anyList(), anyList(), any(), any(), any());
        verifyNoMoreInteractions(getCandlesAPIInformationUseCase);
        verifyNoMoreInteractions(strategyFactoryUseCase);
    }

    @Test
    void testGetFixedLengthCandles() throws Exception {
        String fromLocal = "2023-01-01T00:00:00Z";

        when(getCandlesAPIInformationUseCase.getFixedLengthHistoricalCandlesFromAPI(any(), any(), any(), anyInt()))
                .thenReturn(Collections.emptyList());

        List<CandleStrategiesParams> mockParams = Arrays.asList(CandleStrategiesParams.builder().build(), CandleStrategiesParams.builder().build());
        when(strategyFactoryUseCase.getCandlesStrategiesParameters(anyList(), anyList(), any(), any(), any()))
                .thenReturn(mockParams);

        mockMvc.perform(get("/api/strategies/get-strategy-params-fixed-length-candles")
                        .param("from", fromLocal)
                        .param("figi", "FIGI123")
                        .param("interval", CandleInterval.CANDLE_INTERVAL_DAY.toString())
                        .param("candleLength", "10")
                        .param("strategiesNames", "Strategy1", "Strategy2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.to").exists())
                .andExpect(jsonPath("$.from").exists())
                .andExpect(jsonPath("$.figi").value("FIGI123"))
                .andExpect(jsonPath("$.interval").value(CandleInterval.CANDLE_INTERVAL_DAY.toString()))
                .andExpect(jsonPath("$.strategiesParams").isArray())
                .andExpect(jsonPath("$.strategiesParams.length()").value(2));

        verify(getCandlesAPIInformationUseCase, times(1)).getFixedLengthHistoricalCandlesFromAPI(any(), any(), any(), anyInt());
        verify(strategyFactoryUseCase, times(1)).getCandlesStrategiesParameters(anyList(), anyList(), any(), any(), any());
        verifyNoMoreInteractions(getCandlesAPIInformationUseCase);
        verifyNoMoreInteractions(strategyFactoryUseCase);
    }
    @Test
    void testGetPeriodStrategyParamsWithCandles() throws Exception {
        String jsonBody = "[{\"close\":141.06,\"open\":141.06,\"high\":141.06,\"low\":141.06,\"volume\":415,\"time\":\"2023-01-01T09:00:00Z\"}]";

        List<CandleStrategiesParams> mockParams = Arrays.asList(CandleStrategiesParams.builder().build(), CandleStrategiesParams.builder().build());
        when(strategyFactoryUseCase.getCandlesStrategiesParameters(anyList(), anyList(), any(), any(), any()))
                .thenReturn(mockParams);

        mockMvc.perform(get("/api/strategies/get-strategy-params-with-candles")
                        .param("figi", "FIGI123")
                        .param("interval", CandleInterval.CANDLE_INTERVAL_DAY.toString())
                        .param("strategiesNames", "Strategy1", "Strategy2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.to").exists())
                .andExpect(jsonPath("$.from").exists())
                .andExpect(jsonPath("$.figi").value("FIGI123"))
                .andExpect(jsonPath("$.interval").value(CandleInterval.CANDLE_INTERVAL_DAY.toString()))
                .andExpect(jsonPath("$.strategiesParams").isArray())
                .andExpect(jsonPath("$.strategiesParams.length()").value(2));


        verify(strategyFactoryUseCase, times(1)).getCandlesStrategiesParameters(anyList(), anyList(), any(), any(), any());
        verifyNoMoreInteractions(strategyFactoryUseCase);
    }
}