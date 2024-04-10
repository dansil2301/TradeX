package Eco.TradeX.business.utils.CandleUtils;

import Eco.TradeX.domain.CandleData;

import java.time.Instant;
import java.util.List;

public class EmptyChecker {
    public static Instant getStartingTime(List<CandleData> candles, Instant from) { return candles.isEmpty() ? from : candles.get(0).getTime(); }
}
