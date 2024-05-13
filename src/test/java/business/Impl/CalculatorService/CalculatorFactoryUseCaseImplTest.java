package business.Impl.CalculatorService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.CalculatorService.CalculatorFactoryUseCaseImpl;
import Eco.TradeX.business.Impl.CalculatorService.MA.CalculatorMAUseCaseImpl;
import Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces.CalculatorFactoryUseCase;
import Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces.CalculatorStrategyUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.ParameterContainer;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyFactoryUseCase;
import Eco.TradeX.domain.Calculator.ActionSignal;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.domain.StrategyParams.StrategyNameParameter;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TradeXApplication.class)
class CalculatorFactoryUseCaseImplTest {
    @Mock
    private StrategyFactoryUseCase strategyFactoryUseCase;

    @Mock
    private ClientAPIRepository clientAPIRepository;

    @Mock
    private CalculatorMAUseCaseImpl calculatorMAUseCase;

    @Test
    public void testGetCalculatorNames() {
        List<CalculatorStrategyUseCase> calculatorStrategyUseCases = new ArrayList<>();
        calculatorStrategyUseCases.add(calculatorMAUseCase);

        CalculatorFactoryUseCaseImpl calculatorFactoryUseCase = new CalculatorFactoryUseCaseImpl(calculatorStrategyUseCases,
                strategyFactoryUseCase,
                clientAPIRepository);

        when(calculatorMAUseCase.getStrategyName()).thenReturn("MA");

        List<String> result = calculatorFactoryUseCase.getCalculatorNames();

        assertEquals(1, result.size());
        assertEquals("MA", result.get(0));
    }

    @Test
    void calculateProfitForPeriod() {
        List<CalculatorStrategyUseCase> calculatorStrategyUseCases = new ArrayList<>();
        calculatorStrategyUseCases.add(calculatorMAUseCase);

        when(calculatorMAUseCase.getStrategyName()).thenReturn("MA");

        CalculatorFactoryUseCaseImpl calculatorFactoryUseCase = new CalculatorFactoryUseCaseImpl(calculatorStrategyUseCases,
                strategyFactoryUseCase,
                clientAPIRepository);

        Instant from = Instant.now().minus(30, ChronoUnit.DAYS);
        Instant to = Instant.now();
        String figi = "FIGI";
        CandleInterval interval = CandleInterval.CANDLE_INTERVAL_DAY;
        String strategyName = "MA";
        Double deposit = 1000.0;

        List<CandleData> candles = Arrays.asList(
                CandleData.builder().time(Instant.now()).close(BigDecimal.valueOf(100)).build(),
                CandleData.builder().time(Instant.now().plus(1, ChronoUnit.DAYS)).close(BigDecimal.valueOf(110)).build()
        );

        when(clientAPIRepository.getHistoricalCandles(from, to, figi, interval)).thenReturn(candles);
        doNothing().when(strategyFactoryUseCase).initializeContainers(anyList(), anyString(), any(), any());

        CandleStrategiesParams mockedParams = mock(CandleStrategiesParams.class);
        when(strategyFactoryUseCase.calculateParametersForCandle(any(), anyList(), any()))
                .thenReturn(mockedParams);

        StrategyNameParameter mockedStrategyNameParam = mock(StrategyNameParameter.class);
        when(mockedStrategyNameParam.getStrategyName()).thenReturn("MA");
        when(mockedStrategyNameParam.getParameters()).thenReturn(mock(ParameterContainer.class));

        when(mockedParams.getCandle()).thenReturn(mock(CandleData.class));
        when(mockedParams.getStrategyNameParameters()).thenReturn(Arrays.asList(mockedStrategyNameParam));

        when(calculatorMAUseCase.ActionForThisCandle(any())).thenReturn(ActionSignal.BUY);
        Double profit = calculatorFactoryUseCase.calculateProfitForPeriod(from, to, figi, interval, strategyName, deposit);

        assertEquals(1100, profit);
    }
}