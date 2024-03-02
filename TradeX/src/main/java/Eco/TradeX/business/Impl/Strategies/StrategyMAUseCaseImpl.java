package Eco.TradeX.business.Impl.Strategies;

import Eco.TradeX.business.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.GetStrategyParamsUseCase;
import Eco.TradeX.domain.CandleData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Primary
public class StrategyMAUseCaseImpl implements GetStrategyParamsUseCase {
    private GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;
    private final int extraCandlesNeeded;
    // MA specific parameters
    private final int longMA = 20;
    private final int shortMA = 5;

    public StrategyMAUseCaseImpl(GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase) {
        this.getCandlesAPIInformationUseCase = getCandlesAPIInformationUseCase;
        this.extraCandlesNeeded = this.longMA - 1;
    }

    private void getMovingSum(List<CandleData> allCandles, List<BigDecimal> averageMALong, int i, BigDecimal sumLong, int longMA) {
        for (int j = i; j >= i - (longMA - 1); j--) {
            sumLong = sumLong.add(allCandles.get(j).getClose());
        }
        BigDecimal averageLong = sumLong.divide(BigDecimal.valueOf(longMA), 2, BigDecimal.ROUND_HALF_UP);
        averageMALong.add(averageLong);
    }

    @Override
    public Map<String, List<BigDecimal>> getStrategyParametersForCandles(List<CandleData> candles, Instant from, Instant to, String figi, CandleInterval interval) {
        List<CandleData> extraCandles = getCandlesAPIInformationUseCase.getExtraHistoricalCandlesFromCertainTimeAPI(from, figi, interval, extraCandlesNeeded);
        List<CandleData> allCandles = Stream.concat(extraCandles.stream(), candles.stream()).toList();
        Map<String, List<BigDecimal>> parameter_saver = new HashMap<>();

        List<BigDecimal> averageMALong = new ArrayList<>();
        List<BigDecimal> averageMAShort = new ArrayList<>();
        for (int i = longMA - 1; i < allCandles.size(); i++) {
            BigDecimal sumLong = BigDecimal.ZERO;
            BigDecimal sumShort = BigDecimal.ZERO;

            getMovingSum(allCandles, averageMALong, i, sumLong, longMA);

            getMovingSum(allCandles, averageMAShort, i, sumShort, shortMA);
        }
        parameter_saver.put("long_MA", averageMALong);
        parameter_saver.put("short_MA", averageMAShort);

        return parameter_saver;
    }
}
