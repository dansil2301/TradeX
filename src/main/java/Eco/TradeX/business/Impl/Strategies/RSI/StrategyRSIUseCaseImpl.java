package Eco.TradeX.business.Impl.Strategies.RSI;

import Eco.TradeX.business.GetStrategyParamsUseCase;
import Eco.TradeX.business.utils.CalculationHelper;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.ClientAPIRepository;
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
public class StrategyRSIUseCaseImpl implements GetStrategyParamsUseCase {
    private ClientAPIRepository clientAPIRepository;
    private RSIContainerData rsiContainerData;
    private CandleData prevCandleSaver;
    private final int extraCandlesNeeded;
    private final int periodMA = 20;

    public StrategyRSIUseCaseImpl(ClientAPIRepository clientAPIRepository) {
        this.clientAPIRepository = clientAPIRepository;
        this.extraCandlesNeeded = this.periodMA + 1;
    }

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

    private RSIContainerData initializeContainer(List<CandleData> extraCandles){
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

    public Map<String, BigDecimal> calculateParameterForCandle(CandleData candle) {
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
        } else {
            current_RSI = BigDecimal.ZERO;
        }

         return new HashMap<String, BigDecimal>() {{
            put("RSI", current_RSI);
        }};
    }

    @Override
    public Map<String, List<BigDecimal>> getStrategyParametersForCandles(List<CandleData> candles, Instant from, Instant to, String figi, CandleInterval interval) {
        List<CandleData> extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(from, figi, interval, extraCandlesNeeded);
        rsiContainerData = initializeContainer(extraCandles);
        prevCandleSaver = extraCandles.get(extraCandles.size() - 1);

        Map<String, List<BigDecimal>> parameter_saver = new HashMap<>();
        List<BigDecimal> RSI = new ArrayList<>();
        for (CandleData candle : candles) {
            var current_RSI = calculateParameterForCandle(candle);
            RSI.add(current_RSI.get("RSI"));
        }
        parameter_saver.put("RSI", RSI);

        return parameter_saver;
    }
}
