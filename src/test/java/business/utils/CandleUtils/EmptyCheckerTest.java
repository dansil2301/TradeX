package business.utils.CandleUtils;

import Eco.TradeX.domain.CandleData;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static Eco.TradeX.business.utils.CandleUtils.EmptyChecker.getStartingTime;
import static org.junit.jupiter.api.Assertions.*;

class EmptyCheckerTest {

    @Test
    void getStartingTimeTestFromTime() {
        List<CandleData> candles = new ArrayList<>();
        Instant from = Instant.parse("2023-01-01T00:00:00Z");

        Instant start = getStartingTime(candles, from);

        assertEquals(from, start);
    }

    @Test
    void getStartingTimeTestCandleTime() {
        List<CandleData> candles = new ArrayList<>();
        CandleData candle = CandleData.builder().time(Instant.parse("2023-01-02T00:00:00Z")).build();
        candles.add(candle);

        Instant from = Instant.parse("2023-01-01T00:00:00Z");

        Instant start = getStartingTime(candles, from);

        assertEquals(candle.getTime(), start);
    }
}