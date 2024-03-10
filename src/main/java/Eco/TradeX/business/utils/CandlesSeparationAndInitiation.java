package Eco.TradeX.business.utils;

import Eco.TradeX.business.exceptions.StrategyExceptions;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.impl.tinkoff.ClientTinkoffAPIImpl;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class CandlesSeparationAndInitiation {
    ClientTinkoffAPIImpl clientAPIRepository;

    public List<List<CandleData>> initiateCandlesProperly(List<CandleData> candles, List<CandleData> extraCandles, int extraCandlesNeeded, Instant from, String figi, CandleInterval interval) {
        if (extraCandles == null) {
            extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(from, figi, interval, extraCandlesNeeded);
        }
        return separateExtraCandlesAndCandles(candles, extraCandles, extraCandlesNeeded);
    }

    private List<List<CandleData>> separateExtraCandlesAndCandles(List<CandleData> candles, List<CandleData> extraCandles, int extraCandlesNeeded) {
        int missingExtraCandles = extraCandlesNeeded - extraCandles.size();
        List<CandleData> candlesToAdd = new ArrayList<>(candles.subList(0, Math.min(missingExtraCandles, candles.size())));

        List<CandleData> newExtraCandles = new ArrayList<>(extraCandles);
        newExtraCandles.addAll(candlesToAdd);

        List<CandleData> mutableCandles = new ArrayList<>(candles);
        List<CandleData> mutableCandlesToAdd = new ArrayList<>(candlesToAdd);

        mutableCandles.removeAll(mutableCandlesToAdd);

        List<List<CandleData>> result = new ArrayList<>();
        result.add(newExtraCandles);
        result.add(mutableCandles);
        return result;
    }
}
