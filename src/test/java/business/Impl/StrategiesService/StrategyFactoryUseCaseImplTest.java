package business.Impl.StrategiesService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.CandleService.GetCandlesAPIInformationUseCaseImpl;
import Eco.TradeX.business.Impl.StrategiesService.MA.MAParameterContainer;
import Eco.TradeX.business.Impl.StrategiesService.MA.StrategyMAUseCaseImpl;
import Eco.TradeX.business.Impl.StrategiesService.RSI.RSIParameterContainer;
import Eco.TradeX.business.Impl.StrategiesService.RSI.StrategyRSIUseCaseImpl;
import Eco.TradeX.business.Impl.StrategiesService.StrategyFactoryUseCaseImpl;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyUseCase;
import Eco.TradeX.business.utils.CandleUtils.CandlesSeparationAndInitiation;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.persistence.Impl.CandleRepository.tinkoff.ClientTinkoffAPIImpl;
import TestConfigs.BaseTest;
import business.Impl.CreateCandlesDataFake;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeXApplication.class)
class StrategyFactoryUseCaseImplTest extends BaseTest {
    private final CreateCandlesDataFake createCandlesDataFake = new CreateCandlesDataFake();

    @Mock
    private ClientTinkoffAPIImpl clientAPIRepositoryMock;

    @Mock
    private CandlesSeparationAndInitiation candlesSeparationAndInitiationMock;

    @Mock
    private GetCandlesAPIInformationUseCaseImpl clientMock;

    @InjectMocks
    private StrategyMAUseCaseImpl strategyMAUseCaseMock;

    @Test
    void getCandlesStrategiesParametersMock() {
        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCaseMock);
        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, clientAPIRepositoryMock);

        List<CandleData> mockCandles = createCandlesDataFake.createCandles(30);
        List<CandleData> extraCandles = mockCandles.subList(0, 20);

        when(clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(mockCandles);
        when(clientAPIRepositoryMock.getExtraHistoricalCandlesFromCertainTime(from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN, 20))
                .thenReturn(extraCandles);

        List<CandleData> candles = clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        List<List<CandleData>> separatedCandles = new ArrayList<>();
        separatedCandles.add(extraCandles);
        separatedCandles.add(mockCandles);

        when(candlesSeparationAndInitiationMock.initiateCandlesProperly(candles, extraCandles, 20, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(separatedCandles);

        List<CandleStrategiesParams> allParams = strategyFactoryUseCase.getCandlesStrategiesParameters(Collections.singletonList("MA"), candles, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(mockCandles.size(), allParams.size());
    }

    @Test
    void testGetCandlesStrategiesParametersNotEnoughExtraCandlesMock() {
        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<StrategyUseCase> strategies = Collections.singletonList(strategyMAUseCaseMock);
        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, clientAPIRepositoryMock);
        List<String> strategyNames = Collections.singletonList("MA");
        List<CandleData> mockCandles = createCandlesDataFake.createCandles(30);
        when(clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN)).thenReturn(mockCandles);
        when(clientAPIRepositoryMock.getExtraHistoricalCandlesFromCertainTime(from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN, 20)).thenReturn(mockCandles.subList(0, 20));
        List<CandleData> candles = clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);

        List<List<CandleData>> LstCandleData = new ArrayList<>();
        LstCandleData.add(mockCandles.subList(0, 20));
        LstCandleData.add(mockCandles.subList(20, 30));
        when(candlesSeparationAndInitiationMock.initiateCandlesProperly(candles, mockCandles.subList(0, 20), 20, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN)).thenReturn(LstCandleData);

        List<CandleStrategiesParams> allParams = strategyFactoryUseCase.getCandlesStrategiesParameters(strategyNames, candles, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);

        assertAll(
                () -> assertEquals(candles.size(), allParams.size()),
                () -> assertLongMAIsCorrect(allParams)
        );
    }

    private void assertLongMAIsCorrect(List<CandleStrategiesParams> allParams) {
        CandleStrategiesParams param19 = allParams.get(19);
        CandleStrategiesParams param20 = allParams.get(20);

        MAParameterContainer params19 = (MAParameterContainer) param19.getStrategyNameParameters().get(0).getParameters();
        MAParameterContainer params20 = (MAParameterContainer) param20.getStrategyNameParameters().get(0).getParameters();

        assertNull(params19.getLongMA());
        assertNotNull(params20.getLongMA());
    }

    @Test
    void getCandlesStrategiesParametersNoCandlesEmptyMock() {
        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCaseMock);
        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, clientAPIRepositoryMock);

        List<CandleData> mockCandles = createCandlesDataFake.createCandles(30);
        List<CandleData> extraCandles = mockCandles.subList(0, 20);

        when(clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(new ArrayList<>());
        when(clientAPIRepositoryMock.getExtraHistoricalCandlesFromCertainTime(from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN, 20))
                .thenReturn(extraCandles);

        List<CandleData> candles = clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        List<List<CandleData>> separatedCandles = new ArrayList<>();
        separatedCandles.add(extraCandles);
        separatedCandles.add(mockCandles);

        when(candlesSeparationAndInitiationMock.initiateCandlesProperly(candles, extraCandles, 20, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(separatedCandles);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> strategyFactoryUseCase.getCandlesStrategiesParameters(Collections.singletonList("MA"), candles, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN)
        );


        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: No candles found for this period\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getCandlesStrategiesParametersNoCandlesNullMock() {
        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCaseMock);
        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, clientAPIRepositoryMock);

        List<CandleData> mockCandles = createCandlesDataFake.createCandles(30);
        List<CandleData> extraCandles = mockCandles.subList(0, 20);

        when(clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(null);
        when(clientAPIRepositoryMock.getExtraHistoricalCandlesFromCertainTime(from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN, 20))
                .thenReturn(extraCandles);

        List<CandleData> candles = clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        List<List<CandleData>> separatedCandles = new ArrayList<>();
        separatedCandles.add(extraCandles);
        separatedCandles.add(mockCandles);

        when(candlesSeparationAndInitiationMock.initiateCandlesProperly(candles, extraCandles, 20, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(separatedCandles);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> strategyFactoryUseCase.getCandlesStrategiesParameters(Collections.singletonList("MA"), candles, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN)
        );


        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: No candles found for this period\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getStrategiesTestTrue() {
        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCaseMock);
        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, clientAPIRepositoryMock);

        List<String> names = new ArrayList<>();
        names.add("MA");

        List<StrategyUseCase> recievedStrategies = strategyFactoryUseCase.getStrategies(names);

        assertEquals("MA", recievedStrategies.get(0).getStrategyName());
    }

    @Test
    void getStrategiesTestError() {
        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCaseMock);
        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, clientAPIRepositoryMock);

        List<String> names = new ArrayList<>();
        names.add("Error");

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
        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCaseMock);
        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies, clientAPIRepositoryMock);

        var recievedSNames = strategyFactoryUseCase.getStrategiesNames();

        assertTrue(recievedSNames.contains("MA"));
    }
}