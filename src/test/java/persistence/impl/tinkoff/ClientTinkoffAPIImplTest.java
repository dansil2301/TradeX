package persistence.impl.tinkoff;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.Impl.CandleRepository.tinkoff.ClientTinkoffAPIImpl;
import TestConfigs.BaseTest;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.MarketDataService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeXApplication.class)
class ClientTinkoffAPIImplTest extends BaseTest {
    @Mock
    InvestApi investApiMock;

    @InjectMocks
    ClientTinkoffAPIImpl clientTinkoffAPI;

    @Mock
    MarketDataService marketDataServiceMock;

    @Autowired
    ClientTinkoffAPIImpl client;

    private HistoricCandle createCandlesWithMock(HistoricCandle mockCandle) {
        when(mockCandle.getOpen()).thenAnswer(invocation -> Quotation.newBuilder().setUnits(10).setNano(100000000).build());
        when(mockCandle.getClose()).thenAnswer(invocation -> Quotation.newBuilder().setUnits(10).setNano(100000000).build());
        when(mockCandle.getHigh()).thenAnswer(invocation -> Quotation.newBuilder().setUnits(10).setNano(100000000).build());
        when(mockCandle.getLow()).thenAnswer(invocation -> Quotation.newBuilder().setUnits(10).setNano(100000000).build());
        when(mockCandle.getTime()).thenAnswer(invocation -> {
            Instant now = Instant.now();
            return Timestamp.newBuilder()
                    .setSeconds(now.getEpochSecond())
                    .setNanos(now.getNano())
                    .build();
        });
        when(mockCandle.getVolume()).thenAnswer(invocation -> (long)100);
        return mockCandle;
    }

    @Test
    public void testGetHistoricalCandlesMockCorrect() {
        Instant from = Instant.now();
        Instant to = Instant.now();

        List<HistoricCandle> mockCandles = new ArrayList<>();
        HistoricCandle mockCandle = mock(HistoricCandle.class);
        mockCandles.add(createCandlesWithMock(mockCandle));

        when(investApiMock.getMarketDataService()).thenReturn(marketDataServiceMock);
        when(marketDataServiceMock.getCandlesSync("testFigi", from, to, CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(mockCandles);

        List<CandleData> candles = clientTinkoffAPI.getHistoricalCandles(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(new BigDecimal("10.10"), candles.get(0).getClose().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void getHistoricalCandles1MinuteCorrect() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        var candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        var close = candles.get(0).getClose();
        assertEquals(new BigDecimal("141.060000000"), close);
    }

    @Test
    void testGetHistoricalCandlesMockNoCandles() {
        Instant from = Instant.now();
        Instant to = Instant.now();

        List<HistoricCandle> mockCandles = new ArrayList<>();
        when(investApiMock.getMarketDataService()).thenReturn(marketDataServiceMock);
        when(marketDataServiceMock.getCandlesSync("testFigi", from, to, CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(mockCandles);

        List<CandleData> candles = clientTinkoffAPI.getHistoricalCandles(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(null, candles);
    }

    @Test
    void getHistoricalCandles1MinuteNoCandles() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minusSeconds(1);

        var candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(null, candles);
    }

    @Test
    void getHistoricalCandles1MinuteLengthLimitError() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(500));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN)
        );

        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: Превышен максимальный период запроса для данного интервала свечи. Укажите корректный интервал.\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getExtraHistoricalCandlesFromCertainTime1MinCandle() {
        Instant from = LocalDate.of(2023, 12, 11).atStartOfDay(ZoneId.systemDefault()).toInstant();

        var candles = client.getExtraHistoricalCandlesFromCertainTime(from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN, 20);
        assertEquals(20, candles.size());
    }

    @Test
    void getExtraHistoricalCandlesFromCertainTime1DayCandle() {
        Instant from = LocalDate.of(2023, 12, 11).atStartOfDay(ZoneId.systemDefault()).toInstant();

        var candles = client.getExtraHistoricalCandlesFromCertainTime(from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_DAY, 5);
        assertEquals(5, candles.size());
    }

    @Test
    void getExtraHistoricalCandlesFromCertainTime1MonthCandle() {
        Instant from = LocalDate.of(2023, 12, 11).atStartOfDay(ZoneId.systemDefault()).toInstant();

        var candles = client.getExtraHistoricalCandlesFromCertainTime(from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH, 2);
        assertEquals(2, candles.size());
    }

    @Test
    void getExtraHistoricalCandlesFromCertainTime1MonthCandleOutOfBounds() {
        Instant from = LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        var candles = client.getExtraHistoricalCandlesFromCertainTime(from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH, 2);
        assertEquals(0, candles.size());
    }

    @Test
    void getLastAvailableDateTestCorrect() {
        var lastDate = client.getLastAvailableDate("BBG004730N88");
        Instant testDate = Instant.ofEpochSecond(1520447580);

        assertEquals(testDate, lastDate);
    }

    @Test
    void getLastAvailableDateTestError() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> client.getLastAvailableDate("Error")
        );

        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: Инструмент не найден.Укажите корректный идентификатор инструмента.\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}