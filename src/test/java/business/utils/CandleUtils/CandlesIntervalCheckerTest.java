package business.utils.CandleUtils;

import Eco.TradeX.domain.CandleData;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;

import static Eco.TradeX.business.utils.CandleUtils.CandlesIntervalChecker.isCandleSwitchedToNextInterval;
import static org.junit.jupiter.api.Assertions.*;

class CandlesIntervalCheckerTest {
    @Test
    public void isCandleSwitchedToNextIntervalTest1MinuteTrue() {
        Instant newInstant = Instant.parse("2023-01-02T00:01:00Z");
        Instant oldInstant = Instant.parse("2023-01-02T00:00:00Z");

        CandleData newCandle = CandleData.builder().time(newInstant).build();
        CandleData oldCandle = CandleData.builder().time(oldInstant).build();

        boolean result = isCandleSwitchedToNextInterval(newCandle, oldCandle, CandleInterval.CANDLE_INTERVAL_1_MIN);

        assertTrue(result);
    }

    @Test
    public void isCandleSwitchedToNextIntervalTest1MinuteFalse() {
        Instant newInstant = Instant.parse("2023-01-02T00:00:01Z");
        Instant oldInstant = Instant.parse("2023-01-02T00:00:00Z");

        CandleData newCandle = CandleData.builder().time(newInstant).build();
        CandleData oldCandle = CandleData.builder().time(oldInstant).build();

        boolean result = isCandleSwitchedToNextInterval(newCandle, oldCandle, CandleInterval.CANDLE_INTERVAL_1_MIN);

        assertFalse(result);
    }

    @Test
    public void isCandleSwitchedToNextIntervalTestDayTrue() {
        Instant newInstant = Instant.parse("2023-01-03T00:00:00Z");
        Instant oldInstant = Instant.parse("2023-01-02T00:00:00Z");

        CandleData newCandle = CandleData.builder().time(newInstant).build();
        CandleData oldCandle = CandleData.builder().time(oldInstant).build();

        boolean result = isCandleSwitchedToNextInterval(newCandle, oldCandle, CandleInterval.CANDLE_INTERVAL_DAY);

        assertTrue(result);
    }

    @Test
    public void isCandleSwitchedToNextIntervalTestDayFalse() {
        Instant newInstant = Instant.parse("2023-01-02T10:10:00Z");
        Instant oldInstant = Instant.parse("2023-01-02T00:00:00Z");

        CandleData newCandle = CandleData.builder().time(newInstant).build();
        CandleData oldCandle = CandleData.builder().time(oldInstant).build();

        boolean result = isCandleSwitchedToNextInterval(newCandle, oldCandle, CandleInterval.CANDLE_INTERVAL_DAY);

        assertFalse(result);
    }

    @Test
    public void isCandleSwitchedToNextIntervalTestMonthTrue() {
        Instant newInstant = Instant.parse("2023-02-02T00:00:00Z");
        Instant oldInstant = Instant.parse("2023-01-02T00:00:00Z");

        CandleData newCandle = CandleData.builder().time(newInstant).build();
        CandleData oldCandle = CandleData.builder().time(oldInstant).build();

        boolean result = isCandleSwitchedToNextInterval(newCandle, oldCandle, CandleInterval.CANDLE_INTERVAL_MONTH);

        assertTrue(result);
    }

    @Test
    public void isCandleSwitchedToNextIntervalTestMonthFalse() {
        Instant newInstant = Instant.parse("2023-01-31T00:00:00Z");
        Instant oldInstant = Instant.parse("2023-01-01T00:00:00Z");

        CandleData newCandle = CandleData.builder().time(newInstant).build();
        CandleData oldCandle = CandleData.builder().time(oldInstant).build();

        boolean result = isCandleSwitchedToNextInterval(newCandle, oldCandle, CandleInterval.CANDLE_INTERVAL_MONTH);

        assertFalse(result);
    }
}