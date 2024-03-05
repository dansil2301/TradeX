package Eco.TradeX.business.Impl.Strategies.MA;

import Eco.TradeX.business.ParameterContainer;
import Eco.TradeX.business.StrategyUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.ClientAPIRepository;
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

import static Eco.TradeX.business.utils.CalculationHelper.calculateAverage;

@Service
@Primary
public class StrategyMAUseCaseImpl implements StrategyUseCase {
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
    public ParameterContainer calculateParametersForCandle(CandleData candle) {
        maContainerData.moveByOne(candle.getClose());
        BigDecimal maLongAvg = calculateAverage(maContainerData.getCandlesCloseLong(), RoundingMode.HALF_UP);
        BigDecimal maShortAvg = calculateAverage(maContainerData.getCandlesCloseLong()
                .subList(maContainerData.getCandlesCloseLong().size() - shortMA - 1, maContainerData.getCandlesCloseLong().size() - 1),
                RoundingMode.HALF_UP);

        return MAParameterContainer.builder()
                .longMA(maLongAvg)
                .shortMA(maShortAvg)
                .build();
    }

    @Override
    public List<ParameterContainer> getStrategyParametersForCandles(List<CandleData> candles, Instant from, Instant to, String figi, CandleInterval interval) {
        List<CandleData> extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(from, figi, interval, extraCandlesNeeded);
        maContainerData = initializeContainer(extraCandles);

        List<ParameterContainer> paramContainer = new ArrayList<>();
        for (CandleData candle : candles) {
            var params = calculateParametersForCandle(candle);
            paramContainer.add(params);
        }

        return paramContainer;
    }
}
