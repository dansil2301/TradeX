package business.Impl.CalculatorService.MA;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.CalculatorService.MA.CalculatorMAUseCaseImpl;
import Eco.TradeX.business.Impl.StrategiesService.MA.MAParameterContainer;
import Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces.CalculatorStrategyUseCase;
import Eco.TradeX.domain.Calculator.ActionSignal;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.domain.StrategyParams.StrategyNameParameter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeXApplication.class)
class CalculatorMAUseCaseImplTest {
    private CalculatorMAUseCaseImpl calculatorMAUseCase;

    @Test
    void getStrategyName() {
        calculatorMAUseCase = new CalculatorMAUseCaseImpl();

        String name = calculatorMAUseCase.getStrategyName();

        assertEquals("MA", name);
    }

    @Test
    void actionForThisCandleKeepTest() {
        calculatorMAUseCase = new CalculatorMAUseCaseImpl();

        MAParameterContainer mockParameters = MAParameterContainer.builder()
                .longMA(BigDecimal.TEN)
                .shortMA(BigDecimal.ONE)
                .build();

        StrategyNameParameter strategyNameParam = StrategyNameParameter.builder()
                .strategyName("MA")
                .parameters(mockParameters)
                .build();

        CandleStrategiesParams mockCandleParams = mock(CandleStrategiesParams.class);
        when(mockCandleParams.getStrategyNameParameters()).thenReturn(Collections.singletonList(strategyNameParam));

        ActionSignal action = calculatorMAUseCase.ActionForThisCandle(mockCandleParams);
        assertEquals(ActionSignal.KEEP, action);
    }

    @Test
    void actionForThisCandleBuyTest() {
        calculatorMAUseCase = new CalculatorMAUseCaseImpl();

        MAParameterContainer mockParameters = MAParameterContainer.builder()
                .longMA(BigDecimal.TEN)
                .shortMA(BigDecimal.ONE)
                .build();

        StrategyNameParameter strategyNameParam = StrategyNameParameter.builder()
                .strategyName("MA")
                .parameters(mockParameters)
                .build();

        CandleStrategiesParams mockCandleParams = mock(CandleStrategiesParams.class);
        when(mockCandleParams.getStrategyNameParameters()).thenReturn(Collections.singletonList(strategyNameParam));

        ActionSignal action = calculatorMAUseCase.ActionForThisCandle(mockCandleParams);
        assertEquals(ActionSignal.KEEP, action);

        mockParameters = MAParameterContainer.builder()
                .longMA(BigDecimal.ONE)
                .shortMA(BigDecimal.TEN)
                .build();

        strategyNameParam = StrategyNameParameter.builder()
                .strategyName("MA")
                .parameters(mockParameters)
                .build();

        mockCandleParams = mock(CandleStrategiesParams.class);
        when(mockCandleParams.getStrategyNameParameters()).thenReturn(Collections.singletonList(strategyNameParam));

        action = calculatorMAUseCase.ActionForThisCandle(mockCandleParams);
        assertEquals(ActionSignal.BUY, action);
    }

    @Test
    void actionForThisCandleSellTest() {
        calculatorMAUseCase = new CalculatorMAUseCaseImpl();

        MAParameterContainer mockParameters = MAParameterContainer.builder()
                .longMA(BigDecimal.ONE)
                .shortMA(BigDecimal.TEN)
                .build();

        StrategyNameParameter strategyNameParam = StrategyNameParameter.builder()
                .strategyName("MA")
                .parameters(mockParameters)
                .build();

        CandleStrategiesParams mockCandleParams = mock(CandleStrategiesParams.class);
        when(mockCandleParams.getStrategyNameParameters()).thenReturn(Collections.singletonList(strategyNameParam));

        ActionSignal action = calculatorMAUseCase.ActionForThisCandle(mockCandleParams);
        assertEquals(ActionSignal.KEEP, action);

        mockParameters = MAParameterContainer.builder()
                .longMA(BigDecimal.TEN)
                .shortMA(BigDecimal.ONE)
                .build();

        strategyNameParam = StrategyNameParameter.builder()
                .strategyName("MA")
                .parameters(mockParameters)
                .build();

        mockCandleParams = mock(CandleStrategiesParams.class);
        when(mockCandleParams.getStrategyNameParameters()).thenReturn(Collections.singletonList(strategyNameParam));

        action = calculatorMAUseCase.ActionForThisCandle(mockCandleParams);
        assertEquals(ActionSignal.SELL, action);
    }
}