package Eco.TradeX.persistence.Utils;

import Eco.TradeX.domain.CandleData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueCandlesFilter {
    static public List<CandleData> filterUniqueCandles(List<CandleData> candles) {
        Set<Instant> uniqueTimestamps = new HashSet<>();

        List<CandleData> uniqueCandles = new ArrayList<>();

        for (CandleData candle : candles) {
            if (!uniqueTimestamps.contains(candle.getTime())) {
                uniqueTimestamps.add(candle.getTime());
                uniqueCandles.add(candle);
            }
        }

        return uniqueCandles;
    }
}
