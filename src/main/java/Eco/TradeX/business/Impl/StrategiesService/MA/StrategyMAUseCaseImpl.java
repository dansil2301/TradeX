package Eco.TradeX.business.Impl.StrategiesService.MA;

import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.ParameterContainer;
import Eco.TradeX.business.exceptions.CandlesExceptions;
import Eco.TradeX.business.utils.CandleUtils.CandlesSeparationAndInitiation;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyUseCase;

import static Eco.TradeX.business.utils.CandleUtils.CalculationHelper.calculateAverage;
import static Eco.TradeX.business.utils.CandleUtils.CandlesIntervalChecker.isCandleSwitchedToNextInterval;

@Service
@Primary
public class StrategyMAUseCaseImpl implements StrategyUseCase {
    private ClientAPIRepository clientAPIRepository;
    private CandlesSeparationAndInitiation candlesSeparationAndInitiation;
    private MAContainerData maContainerData;
    private List<CandleData> extraCandlesContainer;
    private CandleData prevCandleSaver;
    private final int extraCandlesNeeded;
    private final int longMA = 20;
    private final int shortMA = 5;

    public StrategyMAUseCaseImpl(ClientAPIRepository clientAPIRepository, CandlesSeparationAndInitiation candlesSeparationAndInitiation) {
        this.clientAPIRepository = clientAPIRepository;
        this.candlesSeparationAndInitiation = candlesSeparationAndInitiation;
        this.extraCandlesNeeded = this.longMA;
    }

    @Override
    public String getStrategyName() {
        return "MA";
    }

    @Override
    public int getExtraCandlesNeeded() {
        return extraCandlesNeeded;
    }

    @Override
    public void initializeContainerForCandleLiveStreaming(String figi, CandleInterval interval, Instant now) {
        List<CandleData> extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(now, figi, interval, extraCandlesNeeded);
        maContainerData = initializeContainer(extraCandles);
        prevCandleSaver = extraCandles.get(extraCandles.size() - 1);
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

    @Override
    public ParameterContainer calculateParametersForCandle(CandleData candle, CandleInterval interval) {
        if (isCandleSwitchedToNextInterval(prevCandleSaver, candle, interval))
        { maContainerData.moveByOne(candle.getClose()); }
        else
        { maContainerData.changeLast(candle.getClose()); }

        prevCandleSaver = candle;

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
    public void initializeExtraCandlesThroughFactory(List<CandleData> extraCandles) {
        extraCandlesContainer = extraCandles;
        prevCandleSaver = extraCandlesContainer.get(extraCandlesContainer.size() - 1);
        maContainerData = initializeContainer(extraCandlesContainer);
    }

    private List<ParameterContainer> initParamContainerInExtraCandlesShortageCase(List<ParameterContainer> paramContainer, int fillGaps) {
        for(int i = 0; i < fillGaps; i ++) {
            paramContainer.add(MAParameterContainer.builder()
                    .longMA(null)
                    .shortMA(null)
                    .build());
        }

        return paramContainer;
    }

    @Override
    public List<ParameterContainer> getStrategyParametersForCandles(List<CandleData> candles, Instant from, String figi, CandleInterval interval) {
        if (candles == null) {
            throw new CandlesExceptions("No candles found for this period");
        }

        int initialCandlesLen = candles.size();
        var newCandlesSep = candlesSeparationAndInitiation.initiateCandlesProperly(candles, extraCandlesContainer, extraCandlesNeeded, from, figi, interval);
        extraCandlesContainer = newCandlesSep.get(0);
        candles = newCandlesSep.get(1);

        maContainerData = initializeContainer(extraCandlesContainer);
        prevCandleSaver = extraCandlesContainer.get(extraCandlesContainer.size() - 1);

        List<ParameterContainer> paramContainer = new ArrayList<>();
        if (initialCandlesLen != candles.size()) {
            paramContainer = initParamContainerInExtraCandlesShortageCase(paramContainer, initialCandlesLen - candles.size());
        }
        for (CandleData candle : candles) {
            var params = calculateParametersForCandle(candle, interval);
            paramContainer.add(params);
        }

        return paramContainer;
    }
}
