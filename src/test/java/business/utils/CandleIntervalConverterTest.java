package business.utils;

import Eco.TradeX.TradeXApplication;
import TestConfigs.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import static Eco.TradeX.business.utils.CandleUtils.CandleIntervalConverter.toSeconds;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TradeXApplication.class)
class CandleIntervalConverterTest extends BaseTest {

    @Test
    void toSecondsTest1Min() {
        int seconds = toSeconds(CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(60, seconds);
    }

    @Test
    void toSecondsTest1Month() {
        int seconds = toSeconds(CandleInterval.CANDLE_INTERVAL_MONTH);
        assertEquals(2629746, seconds);
    }

    @Test
    void toSecondsTestDefault() {
        int seconds = toSeconds(CandleInterval.UNRECOGNIZED);
        assertEquals(80000, seconds);
    }
}