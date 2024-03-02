package Eco.TradeX.business.utils;

import ru.tinkoff.piapi.contract.v1.CandleInterval;

public class CandleIntervalConverter {
    public static int toSeconds(CandleInterval interval){
        return switch (interval) {
            case CANDLE_INTERVAL_1_MIN -> 60;
            case CANDLE_INTERVAL_2_MIN -> 120;
            case CANDLE_INTERVAL_3_MIN -> 180;
            case CANDLE_INTERVAL_5_MIN -> 300;
            case CANDLE_INTERVAL_15_MIN -> 900;
            case CANDLE_INTERVAL_10_MIN -> 600;
            case CANDLE_INTERVAL_30_MIN -> 1800;
            case CANDLE_INTERVAL_HOUR -> 3600;
            case CANDLE_INTERVAL_2_HOUR -> 7200;
            case CANDLE_INTERVAL_4_HOUR -> 14400;
            case CANDLE_INTERVAL_DAY -> 86400;
            case CANDLE_INTERVAL_WEEK -> 604800;
            case CANDLE_INTERVAL_MONTH -> 2629746;
            default -> 86400;
        };
    }
}
