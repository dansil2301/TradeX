package business.utils.CandleUtils;

import Eco.TradeX.TradeXApplication;
import TestConfigs.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import static Eco.TradeX.business.utils.CandleUtils.CandleIntervalConverter.*;
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

    @Test
    void toMaximumFetchPeriodTest1Min() {
        int seconds = toMaximumFetchPeriodInSeconds(CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(86400, seconds);
    }

    @Test
    void toMaximumFetchPeriodTest1Month() {
        int seconds = toMaximumFetchPeriodInSeconds(CandleInterval.CANDLE_INTERVAL_MONTH);
        assertEquals(63072000, seconds);
    }

    @Test
    void toMaximumFetchPeriodTestDefault() {
        int seconds = toMaximumFetchPeriodInSeconds(CandleInterval.UNRECOGNIZED);
        assertEquals(80000, seconds);
    }

    @Test
    void toMaximumFetchPeriodInSecondsTest1Min() {
        int minutes = toMinutesOnlyMinutePeriod(CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(1, minutes);
    }

    @Test
    void toMaximumFetchPeriodInSecondsTestDefault() {
        int minutes = toMinutesOnlyMinutePeriod(CandleInterval.CANDLE_INTERVAL_DAY);
        assertEquals(-1, minutes);
    }

    @Test
    void toHoursOnlyHoursPeriodTest1Hour() {
        int hours = toHoursOnlyHoursPeriod(CandleInterval.CANDLE_INTERVAL_HOUR);
        assertEquals(1, hours);
    }

    @Test
    void toHoursOnlyHoursPeriodTestDefault() {
        int hours = toHoursOnlyHoursPeriod(CandleInterval.CANDLE_INTERVAL_DAY);
        assertEquals(-1, hours);
    }

    @Test
    void toDaysOnlyDaysPeriodTestDay() {
        int days = toDaysOnlyDaysPeriod(CandleInterval.CANDLE_INTERVAL_DAY);
        assertEquals(1, days);
    }

    @Test
    void toDaysOnlyDaysPeriodTestDefault() {
        int days = toDaysOnlyDaysPeriod(CandleInterval.CANDLE_INTERVAL_MONTH);
        assertEquals(-1, days);
    }

    @Test
    void toWeeksOnlyWeeksPeriodTest1Week() {
        int weeks = toWeeksOnlyWeeksPeriod(CandleInterval.CANDLE_INTERVAL_WEEK);
        assertEquals(1, weeks);
    }

    @Test
    void toWeeksOnlyWeeksPeriodTestDefault() {
        int weeks = toWeeksOnlyWeeksPeriod(CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(-1, weeks);
    }

    @Test
    void toMonthsOnlyMonthsPeriod1Month() {
        int months = toMonthsOnlyMonthsPeriod(CandleInterval.CANDLE_INTERVAL_MONTH);
        assertEquals(1, months);
    }

    @Test
    void toMonthsOnlyMonthsPeriodTestDefault() {
        int months = toMonthsOnlyMonthsPeriod(CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(-1, months);
    }
}