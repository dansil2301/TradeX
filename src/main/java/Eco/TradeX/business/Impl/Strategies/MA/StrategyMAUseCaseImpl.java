package Eco.TradeX.business.Impl.Strategies.MA;

import Eco.TradeX.business.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.GetStrategyParamsUseCase;
import Eco.TradeX.business.Impl.Strategies.RSI.RSIContainerData;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.ClientAPIRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Eco.TradeX.business.utils.CalculationHelper.calculateAverage;

@Service
@Primary
public class StrategyMAUseCaseImpl implements GetStrategyParamsUseCase {
    private ClientAPIRepository clientAPIRepository;
    private MAContainerData maContainerData;
    private final int extraCandlesNeeded;
    private final int longMA = 20;
    private final int shortMA = 5;

    public StrategyMAUseCaseImpl(ClientAPIRepository clientAPIRepository) {
        this.clientAPIRepository = clientAPIRepository;
        this.extraCandlesNeeded = this.longMA;
    }

    @Override
    public String getStrategyName() {
        return "MA";
    }

    @Override
    public void initializeContainerForCandleLiveStreaming(String figi, CandleInterval interval) {
        Instant now = Instant.now();
        List<CandleData> extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(now, figi, interval, extraCandlesNeeded);
        maContainerData = initializeContainer(extraCandles);
    }

    private MAContainerData initializeContainer(List<CandleData> extraCandles) {
        List<BigDecimal> closePrices = extraCandles.stream()
                .map(CandleData::getClose)
                .toList();

        maContainerData = MAContainerData.builder()
                .candlesCloseLong(closePrices)
                .build();

        return maContainerData;
    }

    private void getMovingSum(List<CandleData> allCandles, List<BigDecimal> averageMALong, int i, BigDecimal sumLong, int longMA) {
        for (int j = i; j >= i - (longMA - 1); j--) {
            sumLong = sumLong.add(allCandles.get(j).getClose());
        }
        BigDecimal averageLong = sumLong.divide(BigDecimal.valueOf(longMA), 2, BigDecimal.ROUND_HALF_UP);
        averageMALong.add(averageLong);
    }

    @Override
    public Map<String, BigDecimal> calculateParametersForCandle(CandleData candle) {
        maContainerData.moveByOne(candle.getClose());
        BigDecimal maLongAvg = calculateAverage(maContainerData.getCandlesCloseLong(), RoundingMode.HALF_UP);
        BigDecimal maShortAvg = calculateAverage(maContainerData.getCandlesCloseLong()
                .subList(maContainerData.getCandlesCloseLong().size() - shortMA - 1, maContainerData.getCandlesCloseLong().size() - 1),
                RoundingMode.HALF_UP);

        return new HashMap<String, BigDecimal>() {{
            put("long_MA", maLongAvg);
            put("short_MA", maShortAvg);
        }};
    }

    @Override
    public Map<String, List<BigDecimal>> getStrategyParametersForCandles(List<CandleData> candles, Instant from, Instant to, String figi, CandleInterval interval) {
        List<CandleData> extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(from, figi, interval, extraCandlesNeeded);
        maContainerData = initializeContainer(extraCandles);

        Map<String, List<BigDecimal>> parameter_saver = new HashMap<>();
        List<BigDecimal> averageMALong = new ArrayList<>();
        List<BigDecimal> averageMAShort = new ArrayList<>();
        for (CandleData candle : candles) {
            var longShort = calculateParametersForCandle(candle);
            averageMALong.add(longShort.get("long_MA"));
            averageMAShort.add(longShort.get("short_MA"));
        }
        parameter_saver.put("long_MA", averageMALong);
        parameter_saver.put("short_MA", averageMAShort);

        return parameter_saver;
    }
}
