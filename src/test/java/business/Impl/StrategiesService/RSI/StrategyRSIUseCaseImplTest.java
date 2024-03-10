package business.Impl.StrategiesService.RSI;

import Eco.TradeX.business.Impl.StrategiesService.RSI.RSIParameterContainer;
import Eco.TradeX.business.Impl.StrategiesService.RSI.StrategyRSIUseCaseImpl;
import Eco.TradeX.business.utils.CandlesSeparationAndInitiation;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.impl.tinkoff.CandleRepository.ClientTinkoffAPIImpl;
import Eco.TradeX.persistence.impl.tinkoff.CandleRepository.TokenManagerTinkoffImpl;
import org.junit.jupiter.api.Test;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategyRSIUseCaseImplTest {

    @Test
    void getStrategyParametersForCandles1MinCandle1DaysLongMA() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyMAUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);

        var parameters = strategyMAUseCase.getStrategyParametersForCandles(candles, from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(candles.size(), parameters.size());
    }

    @Test
    void getStrategyParametersForCandles5MinCandle1DaysLongMA() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyMAUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_5_MIN);

        var parameters = strategyMAUseCase.getStrategyParametersForCandles(candles, from,"BBG004730N88", CandleInterval.CANDLE_INTERVAL_5_MIN);
        assertEquals(candles.size(), parameters.size());
    }

    @Test
    void getStrategyParametersForCandlesWeekCandle1DaysLongMA() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyMAUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(14));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_WEEK);

        var parameters = strategyMAUseCase.getStrategyParametersForCandles(candles, from,"BBG004730N88", CandleInterval.CANDLE_INTERVAL_WEEK);
        assertEquals(candles.size(), parameters.size());
    }

    @Test
    void getStrategyParametersForCandles1MonthCandle1DaysLongMANotEnoughExtraCandlesCase() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyMAUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        Instant to = LocalDate.of(2002, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(720));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH);

        var parameters = strategyMAUseCase.getStrategyParametersForCandles(candles, from,"BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH);
        var parameterNull = (RSIParameterContainer)parameters.get(20);
        var parameterNotNull = (RSIParameterContainer)parameters.get(21);
        assertEquals(candles.size(), parameters.size());
        assertNull(parameterNull.getRSI());
        assertEquals(new BigDecimal(44).setScale(2, BigDecimal.ROUND_HALF_UP)
                , parameterNotNull.getRSI().setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    void getStrategyParametersForCandles1MonthCandle1DaysLongMANoCandlesCase() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyMAUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        Instant to = LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> strategyMAUseCase.getStrategyParametersForCandles(candles, from,"BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH)
        );

        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: No candles found for this period\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void initializeExtraCandlesThroughFactorySimpleTest() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyMAUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        strategyMAUseCase.initializeExtraCandlesThroughFactory(new ArrayList<>());

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(14));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_WEEK);

        var parameters = strategyMAUseCase.getStrategyParametersForCandles(candles, from,"BBG004730N88", CandleInterval.CANDLE_INTERVAL_WEEK);
        assertEquals(candles.size(), parameters.size());
    }

    @Test
    void calculateParametersForCandle1MinTest() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyMAUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        strategyMAUseCase.initializeContainerForCandleLiveStreaming("BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        CandleData candle = candles.get(candles.size() - 1);

        var parameter = strategyMAUseCase.calculateParametersForCandle(candle);
        assertEquals(true, parameter != null);
    }

    @Test
    void calculateParametersForCandle5MinTest() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyMAUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        strategyMAUseCase.initializeContainerForCandleLiveStreaming("BBG004730N88", CandleInterval.CANDLE_INTERVAL_5_MIN);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_5_MIN);
        CandleData candle = candles.get(candles.size() - 1);

        var parameter = strategyMAUseCase.calculateParametersForCandle(candle);
        assertEquals(true, parameter != null);
    }

    @Test
    void calculateParametersForCandleWeekTest() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyMAUseCase = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        strategyMAUseCase.initializeContainerForCandleLiveStreaming("BBG004730N88", CandleInterval.CANDLE_INTERVAL_WEEK);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(10));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_WEEK);
        CandleData candle = candles.get(candles.size() - 1);

        var parameter = strategyMAUseCase.calculateParametersForCandle(candle);
        assertEquals(true, parameter != null);
    }
}