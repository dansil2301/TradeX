package persistence.impl.tinkoff;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.exceptions.CandlesExceptions;
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
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.core.InstrumentsService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeXApplication.class)
class ClientTinkoffAPIImplTest extends BaseTest {
    @Mock
    InvestApi investApiMock;

    @Mock
    InstrumentsService getInstrumentsServiceMock;

    @Mock
    MarketDataService marketDataServiceMock;

    @InjectMocks
    ClientTinkoffAPIImpl clientTinkoffAPI;

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
        Instant to = Instant.now().plusSeconds(100);

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
    void testGetHistoricalCandlesMockNoCandles() {
        Instant from = Instant.now();
        Instant to = from;

        List<HistoricCandle> mockCandles = new ArrayList<>();
        when(investApiMock.getMarketDataService()).thenReturn(marketDataServiceMock);
        when(marketDataServiceMock.getCandlesSync("testFigi", from, to, CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(mockCandles);

        List<CandleData> candles = clientTinkoffAPI.getHistoricalCandles(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(null, candles);
    }

    @Test
    void testGetHistoricalCandlesMockLengthLimitError() {
        Instant from = Instant.now();
        Instant to = Instant.now().plusSeconds(100);

        when(investApiMock.getMarketDataService()).thenReturn(marketDataServiceMock);
        when(marketDataServiceMock.getCandlesSync("testFigi", from, to, CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenThrow(new RuntimeException("Error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clientTinkoffAPI.getHistoricalCandles(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN)
        );

        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: Error\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getExtraHistoricalCandlesFromCertainTimeCandleMock() {
        Instant from = Instant.now();
        Timestamp timestamp2007 = Timestamp.newBuilder()
                .setSeconds(java.time.Year.of(2007).atDay(1).atStartOfDay(java.time.ZoneOffset.UTC).toEpochSecond())
                .build();

        List<HistoricCandle> mockCandles = new ArrayList<>();
        HistoricCandle mockCandle = mock(HistoricCandle.class);
        mockCandles.add(createCandlesWithMock(mockCandle));

        when(investApiMock.getMarketDataService()).thenReturn(marketDataServiceMock);
        when(investApiMock.getInstrumentsService()).thenReturn(getInstrumentsServiceMock);

        Instrument instrumentMock = mock(Instrument.class);
        when(getInstrumentsServiceMock.getInstrumentByFigiSync("testFigi")).thenReturn(instrumentMock);

        when(instrumentMock.getFirst1MinCandleDate()).thenReturn(timestamp2007);

        when(marketDataServiceMock.getCandlesSync(eq("testFigi"), any(Instant.class), any(Instant.class), eq(CandleInterval.CANDLE_INTERVAL_1_MIN)))
                .thenReturn(mockCandles);

        List<CandleData> candles = clientTinkoffAPI.getExtraHistoricalCandlesFromCertainTime(from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN, 1);
        assertEquals(1, candles.size());
    }

    @Test
    public void getExtraHistoricalCandlesFromCertainTimeCandleInstrumentErrorMock() {
        Instant from = Instant.now();
        Timestamp timestamp2007 = Timestamp.newBuilder()
                .setSeconds(java.time.Year.of(2007).atDay(1).atStartOfDay(java.time.ZoneOffset.UTC).toEpochSecond())
                .build();

        when(investApiMock.getMarketDataService()).thenReturn(marketDataServiceMock);
        when(investApiMock.getInstrumentsService()).thenReturn(getInstrumentsServiceMock);

        Instrument instrumentMock = mock(Instrument.class);
        when(getInstrumentsServiceMock.getInstrumentByFigiSync("testFigi"))
                .thenThrow(new RuntimeException("Error"));

        when(instrumentMock.getFirst1MinCandleDate()).thenReturn(timestamp2007);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () ->         clientTinkoffAPI.getExtraHistoricalCandlesFromCertainTime(from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN, 1)
        );

        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: Error\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getLastAvailableDateTestCorrectMock() {
        Timestamp timestamp2007 = Timestamp.newBuilder()
                .setSeconds(java.time.Year.of(2007).atDay(1).atStartOfDay(java.time.ZoneOffset.UTC).toEpochSecond())
                .build();

        when(investApiMock.getInstrumentsService()).thenReturn(getInstrumentsServiceMock);

        Instrument instrumentMock = mock(Instrument.class);
        when(getInstrumentsServiceMock.getInstrumentByFigiSync("testFigi"))
                .thenReturn(instrumentMock);

        when(instrumentMock.getFirst1MinCandleDate()).thenReturn(timestamp2007);

        var lastDate = clientTinkoffAPI.getLastAvailableDate("testFigi");

        Instant testDate = Instant.ofEpochSecond(timestamp2007.getSeconds(), timestamp2007.getNanos());

        assertEquals(testDate, lastDate);
    }

    @Test
    void getLastAvailableDateTestErrorMock() {
        when(investApiMock.getInstrumentsService()).thenReturn(getInstrumentsServiceMock);

        Instrument instrumentMock = mock(Instrument.class);
        when(getInstrumentsServiceMock.getInstrumentByFigiSync("testFigi"))
                .thenReturn(instrumentMock);

        when(instrumentMock.getFirst1MinCandleDate())
                .thenThrow(new RuntimeException("Error"));

        CandlesExceptions exception = assertThrows(
                CandlesExceptions.class,
                () -> clientTinkoffAPI.getLastAvailableDate("testFigi")
        );

        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: Error\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}