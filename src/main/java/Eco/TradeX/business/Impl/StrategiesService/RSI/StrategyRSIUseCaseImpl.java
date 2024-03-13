package Eco.TradeX.business.Impl.StrategiesService.RSI;

import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.ParameterContainer;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyUseCase;
import Eco.TradeX.business.exceptions.CandlesExceptions;
import Eco.TradeX.business.utils.CandleUtils.CandlesSeparationAndInitiation;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static Eco.TradeX.business.utils.CandleUtils.CalculationHelper.calculateAverage;

@Service
public class StrategyRSIUseCaseImpl implements StrategyUseCase {
    private ClientAPIRepository clientAPIRepository;
    private CandlesSeparationAndInitiation candlesSeparationAndInitiation;
    private RSIContainerData rsiContainerData;
    private List<CandleData> extraCandlesContainer;
    private CandleData prevCandleSaver;
    private final int extraCandlesNeeded;
    private final int periodMA = 20;

    public StrategyRSIUseCaseImpl(ClientAPIRepository clientAPIRepository, CandlesSeparationAndInitiation candlesSeparationAndInitiation) {
        this.clientAPIRepository = clientAPIRepository;
        this.candlesSeparationAndInitiation = candlesSeparationAndInitiation;
        this.extraCandlesNeeded = this.periodMA + 1;
    }

    @Override
    public String getStrategyName() {
        return "RSI";
    }

    @Override
    public int getExtraCandlesNeeded() {
        return extraCandlesNeeded;
    }

    @Override
    public void initializeContainerForCandleLiveStreaming(String figi, CandleInterval interval) {
        rsiContainerData = RSIContainerData.builder()
                .gain(new ArrayList<>())
                .loss(new ArrayList<>())
                .build();
        Instant now = Instant.now();
        List<CandleData> extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(now, figi, interval, extraCandlesNeeded);
        rsiContainerData = initializeContainer(extraCandles);
        prevCandleSaver = extraCandles.get(extraCandles.size() - 1);
    }

    private RSIContainerData initializeContainer(List<CandleData> extraCandles) {
        RSIContainerData gainLossContainer = RSIContainerData.builder()
                .gain(new ArrayList<>())
                .loss(new ArrayList<>())
                .build();

        for (int i = 1; i < extraCandles.size(); i++) {
            List<BigDecimal> gainLoss = getGainLossCurrentCandle(extraCandles.get(i - 1), extraCandles.get(i));
            gainLossContainer.getGain().add(gainLoss.get(0));
            gainLossContainer.getLoss().add(gainLoss.get(1));
        }

        return gainLossContainer;
    }

    private List<BigDecimal> getGainLossCurrentCandle(CandleData prevCandle, CandleData currCandle) {
        BigDecimal gainPrice;
        BigDecimal lossPrice;
        if (prevCandle.getClose().compareTo(currCandle.getClose()) < 0) {
            gainPrice = currCandle.getClose().subtract(prevCandle.getClose());
            lossPrice = new BigDecimal(0);
        }
        else if (currCandle.getClose().compareTo(prevCandle.getClose()) < 0) {
            gainPrice = new BigDecimal(0);
            lossPrice = prevCandle.getClose().subtract(currCandle.getClose());
        }
        else {
            gainPrice = new BigDecimal(0);
            lossPrice = new BigDecimal(0);
        }

        List<BigDecimal> gainLoss = new ArrayList<>();
        gainLoss.add(gainPrice);
        gainLoss.add(lossPrice);
        return gainLoss;
    }

    @Override
    public ParameterContainer calculateParametersForCandle(CandleData candle) {
        List<BigDecimal> gainLoss = getGainLossCurrentCandle(prevCandleSaver, candle);
        rsiContainerData.moveByOne(gainLoss.get(0), gainLoss.get(1));

        prevCandleSaver = candle;

        BigDecimal avgGain = calculateAverage(rsiContainerData.getGain(), RoundingMode.HALF_UP);
        BigDecimal avgLoss = calculateAverage(rsiContainerData.getLoss(), RoundingMode.HALF_UP);

        BigDecimal RS = new BigDecimal(100);
        RS = !avgLoss.equals(BigDecimal.ZERO) ? avgGain.divide(avgLoss, BigDecimal.ROUND_HALF_UP) : RS;

        BigDecimal divisor = BigDecimal.ONE.add(RS);
        BigDecimal current_RSI;
        if (!divisor.equals(BigDecimal.ZERO)) {
            current_RSI = BigDecimal.valueOf(100).subtract(
                    BigDecimal.valueOf(100).divide(divisor, BigDecimal.ROUND_HALF_UP)
            );
        }
        else {
            current_RSI = BigDecimal.ZERO;
        }

         return RSIParameterContainer.builder()
                 .RSI(current_RSI)
                 .build();
    }

    public void initializeExtraCandlesThroughFactory(List<CandleData> extraCandles) {
        extraCandlesContainer = extraCandles;
    }

    private List<ParameterContainer> initParamContainerInExtraCandlesShortageCase(List<ParameterContainer> paramContainer, int fillGaps) {
        for(int i = 0; i < fillGaps; i ++) {
            paramContainer.add(RSIParameterContainer.builder()
                    .RSI(null)
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

        rsiContainerData = initializeContainer(extraCandlesContainer);
        prevCandleSaver = extraCandlesContainer.get(extraCandlesContainer.size() - 1);

        List<ParameterContainer> paramContainer = new ArrayList<>();
        if (initialCandlesLen != candles.size()) {
            paramContainer = initParamContainerInExtraCandlesShortageCase(paramContainer, initialCandlesLen - candles.size());
        }
        for (CandleData candle : candles) {
            var params = calculateParametersForCandle(candle);
            paramContainer.add(params);
        }

        return paramContainer;
    }
}
