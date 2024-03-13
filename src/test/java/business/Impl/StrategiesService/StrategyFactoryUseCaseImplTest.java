package business.Impl.StrategiesService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.StrategiesService.MA.MAParameterContainer;
import Eco.TradeX.business.Impl.StrategiesService.MA.StrategyMAUseCaseImpl;
import Eco.TradeX.business.Impl.StrategiesService.RSI.RSIParameterContainer;
import Eco.TradeX.business.Impl.StrategiesService.RSI.StrategyRSIUseCaseImpl;
import Eco.TradeX.business.Impl.StrategiesService.StrategyFactoryUseCaseImpl;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyUseCase;
import Eco.TradeX.business.utils.CandleUtils.CandlesSeparationAndInitiation;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.persistence.impl.CandleRepository.tinkoff.ClientTinkoffAPIImpl;
import Eco.TradeX.persistence.impl.CandleRepository.tinkoff.TokenManagerTinkoffImpl;
import TestConfigs.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TradeXApplication.class)
class StrategyFactoryUseCaseImplTest extends BaseTest {
    @Autowired
    private TokenManagerTinkoffImpl tokenManager;

    @Test
    void getCandlesStrategiesParameters() {
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client, candlesSeparationAndInitiation);
        StrategyRSIUseCaseImpl strategyRSIUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCase);
        strategies.add(strategyRSIUseCase);

        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, client);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));
        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);

        List<String> strategyNames = new ArrayList<>();
        strategyNames.add("RSI");
        strategyNames.add("MA");

        var allParams = strategyFactoryUseCase.getCandlesStrategiesParameters(strategyNames, candles, from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(candles.size(), allParams.size());
    }

    @Test
    void getCandlesStrategiesParametersNotEnoughExtraCandles() {
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client, candlesSeparationAndInitiation);
        StrategyRSIUseCaseImpl strategyRSIUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCase);
        strategies.add(strategyRSIUseCase);

        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, client);

        Instant to = LocalDate.of(2002, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(720));
        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH);

        List<String> strategyNames = new ArrayList<>();
        strategyNames.add("RSI");
        strategyNames.add("MA");

        var allParams = strategyFactoryUseCase.getCandlesStrategiesParameters(strategyNames, candles, from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH);
        assertEquals(candles.size(), allParams.size());
    }

    @Test
    void getCandlesStrategiesParametersNoCandlesEmpty() {
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client, candlesSeparationAndInitiation);
        StrategyRSIUseCaseImpl strategyRSIUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCase);
        strategies.add(strategyRSIUseCase);

        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, client);

        Instant to = LocalDate.of(2002, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(720));
        List<CandleData> candles = new ArrayList<>();

        List<String> strategyNames = new ArrayList<>();
        strategyNames.add("RSI");
        strategyNames.add("MA");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> strategyFactoryUseCase.getCandlesStrategiesParameters(strategyNames, candles, from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH)
        );


        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: No candles found for this period\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getCandlesStrategiesParametersNoCandlesNull() {
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client, candlesSeparationAndInitiation);
        StrategyRSIUseCaseImpl strategyRSIUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCase);
        strategies.add(strategyRSIUseCase);

        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, client);

        Instant to = LocalDate.of(2002, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(720));
        List<CandleData> candles = null;

        List<String> strategyNames = new ArrayList<>();
        strategyNames.add("RSI");
        strategyNames.add("MA");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> strategyFactoryUseCase.getCandlesStrategiesParameters(strategyNames, candles, from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH)
        );


        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: No candles found for this period\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getCandlesStrategiesParametersAllEmptyCandles() {
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client, candlesSeparationAndInitiation);
        StrategyRSIUseCaseImpl strategyRSIUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCase);
        strategies.add(strategyRSIUseCase);

        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, client);

        Instant to = LocalDate.of(2000, 4, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(180));
        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH);

        List<String> strategyNames = new ArrayList<>();
        strategyNames.add("RSI");
        strategyNames.add("MA");

        var allParams = strategyFactoryUseCase.getCandlesStrategiesParameters(strategyNames, candles, from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH);
        CandleStrategiesParams params = allParams.get(2);
        var strategyParams = params.getStrategyNameParameters();

        var param1 = strategyParams.get(0);
        var param2 = strategyParams.get(1);

        var MA = (MAParameterContainer)param1.getParameters();
        var RSI = (RSIParameterContainer)param2.getParameters();

        assertEquals(candles.size(), allParams.size());
        assertNull(MA.getLongMA());
        assertNull(RSI.getRSI());
    }

    @Test
    void getStrategiesTestTrue() {
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client, candlesSeparationAndInitiation);
        StrategyRSIUseCaseImpl strategyRSIUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCase);
        strategies.add(strategyRSIUseCase);

        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, client);

        List<String> names = new ArrayList<>();
        names.add("RSI");

        var recievedStrategies = strategyFactoryUseCase.getStrategies(names);

        assertEquals("RSI", recievedStrategies.get(0).getStrategyName());
    }

    @Test
    void getStrategiesTestError() {
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client, candlesSeparationAndInitiation);
        StrategyRSIUseCaseImpl strategyRSIUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCase);
        strategies.add(strategyRSIUseCase);

        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, client);

        List<String> names = new ArrayList<>();
        names.add("Error???");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> strategyFactoryUseCase.getStrategies(names)
        );

        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Strategy Error: Some strategy names from the list don't exist\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getStrategiesNamesTest() {
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client, candlesSeparationAndInitiation);
        StrategyRSIUseCaseImpl strategyRSIUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCase);
        strategies.add(strategyRSIUseCase);

        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, client);

        var recievedSNames = strategyFactoryUseCase.getStrategiesNames();

        assertTrue(recievedSNames.contains("RSI"));
        assertTrue(recievedSNames.contains("MA"));
    }
}