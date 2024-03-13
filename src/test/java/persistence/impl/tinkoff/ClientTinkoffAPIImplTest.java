package persistence.impl.tinkoff;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.persistence.impl.CandleRepository.tinkoff.ClientTinkoffAPIImpl;
import TestConfigs.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = TradeXApplication.class)
class ClientTinkoffAPIImplTest extends BaseTest {
    @Autowired
    private ClientTinkoffAPIImpl client;

    @Test
    void getHistoricalCandles1MinuteCorrect() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        var candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        var close = candles.get(0).getClose();
        assertEquals(new BigDecimal("141.060000000"), close);
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