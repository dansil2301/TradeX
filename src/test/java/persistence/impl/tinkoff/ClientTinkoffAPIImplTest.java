package persistence.impl.tinkoff;

import Eco.TradeX.persistence.impl.tinkoff.ClientTinkoffAPIImpl;
import Eco.TradeX.persistence.impl.tinkoff.TokenManagerTinkoffImpl;
import org.junit.jupiter.api.Test;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ClientTinkoffAPIImplTest {
    @Test
    void getHistoricalCandles1MinuteCorrect() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);

        // period _from _to
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        var candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        var close = candles.get(0).getClose();
        assertEquals(new BigDecimal("141.060000000"), close);
    }

    @Test
    void getHistoricalCandles1MinuteNoCandles() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);

        // period _from _to
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minusSeconds(1);

        var candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(null, candles);
    }

    @Test
    void getHistoricalCandles1MinuteError() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);

        // period _from _to
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(500));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN)
        );

        String expectedMessage = "Error fetching historical candles";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getExtraHistoricalCandlesFromCertainTime1MinCandle() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);

        Instant from = LocalDate.of(2023, 12, 11).atStartOfDay(ZoneId.systemDefault()).toInstant();

        var candles = client.getExtraHistoricalCandlesFromCertainTime(from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN, 20);
        assertEquals(20, candles.size());
    }

    @Test
    void getExtraHistoricalCandlesFromCertainTime1DayCandle() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);

        Instant from = LocalDate.of(2023, 12, 11).atStartOfDay(ZoneId.systemDefault()).toInstant();

        var candles = client.getExtraHistoricalCandlesFromCertainTime(from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_DAY, 5);
        assertEquals(5, candles.size());
    }

    @Test
    void getExtraHistoricalCandlesFromCertainTime1MonthCandle() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);

        Instant from = LocalDate.of(2023, 12, 11).atStartOfDay(ZoneId.systemDefault()).toInstant();

        var candles = client.getExtraHistoricalCandlesFromCertainTime(from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_MONTH, 2);
        assertEquals(2, candles.size());
    }
}