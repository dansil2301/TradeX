package Eco.TradeX.business.utils.CandleUtils;

import Eco.TradeX.domain.CandleData;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneOffset;

import static Eco.TradeX.business.utils.CandleUtils.CandleIntervalConverter.*;

public class CandlesIntervalChecker {
    public static boolean isCandleSwitchedToNextInterval(CandleData newCandle, CandleData oldCandle, CandleInterval interval) {
        Instant newTime = newCandle.getTime();
        Instant oldTime = oldCandle.getTime();
        Duration duration = Duration.between(oldTime, newTime);

        if (toMinutesOnlyMinutePeriod(interval) != -1) {
            Duration intervalDuration = Duration.ofMinutes(toMinutesOnlyMinutePeriod(interval));
            return duration.compareTo(intervalDuration) >= 0;
        }
        else if (toHoursOnlyHoursPeriod(interval) != -1) {
            long intervalHours = toHoursOnlyHoursPeriod(interval);
            long durationHours = duration.toHours();
            return durationHours >= intervalHours;
        }
        else if (toDaysOnlyDaysPeriod(interval) != -1){
            long intervalDays = toDaysOnlyDaysPeriod(interval);
            long durationDays = duration.toDays();
            return durationDays >= intervalDays;
        }
        else if (toWeeksOnlyWeeksPeriod(interval) != -1) {
            long intervalWeeks = toWeeksOnlyWeeksPeriod(interval);
            long durationWeeks = duration.toDays() / 7;
            return durationWeeks >= intervalWeeks;
        } else if (toMonthsOnlyMonthsPeriod(interval) != -1) {
            Period period = Period.between(oldTime.atZone(ZoneOffset.UTC).toLocalDate(), newTime.atZone(ZoneOffset.UTC).toLocalDate());
            long intervalMonths = toMonthsOnlyMonthsPeriod(interval);
            long durationMonths = period.toTotalMonths();
            return durationMonths >= intervalMonths;
        } else {
            return false;
        }
    }
}
