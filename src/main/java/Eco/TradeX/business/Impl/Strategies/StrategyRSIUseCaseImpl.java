package Eco.TradeX.business.Impl.Strategies;

import Eco.TradeX.business.GetStrategyParamsUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.ClientAPIRepository;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StrategyRSIUseCaseImpl implements GetStrategyParamsUseCase {
    private ClientAPIRepository clientAPIRepository;
    private final int extraCandlesNeeded;
    // MA specific parameters
    private final int periodMA = 20;

    public StrategyRSIUseCaseImpl(ClientAPIRepository clientAPIRepository) {
        this.clientAPIRepository = clientAPIRepository;
        this.extraCandlesNeeded = this.periodMA + 1;
    }

    private Map<String, List<BigDecimal>> initializeParamContainer(List<CandleData> extraCandles){
        HashMap<String, List<BigDecimal>> gainLossContainer = new HashMap<String, List<BigDecimal>>() {{
            put("gain", new ArrayList<BigDecimal>());
            put("loss", new ArrayList<BigDecimal>());
        }};

        for (int i = 1; i < extraCandles.size(); i++) {
            if (extraCandles.get(i - 1).getClose().compareTo(extraCandles.get(i).getClose()) < 0) {
                gainLossContainer.get("gain").add(extraCandles.get(i).getClose().subtract(extraCandles.get(i - 1).getClose()));
                gainLossContainer.get("loss").add(new BigDecimal(0));
            }
            else if (extraCandles.get(i).getClose().compareTo(extraCandles.get(i - 1).getClose()) < 0) {
                gainLossContainer.get("loss").add(extraCandles.get(i - 1).getClose().subtract(extraCandles.get(i).getClose()));
                gainLossContainer.get("gain").add(new BigDecimal(0));
            }
            else {
                gainLossContainer.get("gain").add(new BigDecimal(0));
                gainLossContainer.get("loss").add(new BigDecimal(0));
            }
        }

        return gainLossContainer;
    }

    private void moveParamContainer(Map<String, List<BigDecimal>> container, BigDecimal gainPrice, BigDecimal lossPrice) {
        container.get("gain").remove(0);
        container.get("loss").remove(0);

        container.get("gain").add(gainPrice);
        container.get("loss").add(lossPrice);
    }

    private BigDecimal calculateAverage(List<BigDecimal> values) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            sum = sum.add(value);
        }
        return sum.divide(BigDecimal.valueOf(values.size()));
    }

    @Override
    public Map<String, List<BigDecimal>> getStrategyParametersForCandles(List<CandleData> candles, Instant from, Instant to, String figi, CandleInterval interval) {
        List<CandleData> extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(from, figi, interval, extraCandlesNeeded);
        List<CandleData> allCandles = new ArrayList<>(candles);
        allCandles.add(0, extraCandles.get(extraCandles.size() - 1));
        var container = initializeParamContainer(extraCandles);
        Map<String, List<BigDecimal>> parameter_saver = new HashMap<>();

        List<BigDecimal> RSI = new ArrayList<>();
        for (int i = 1; i < allCandles.size(); i++) {
            BigDecimal avgGain = calculateAverage(container.get("gain"));
            BigDecimal avgLoss = calculateAverage(container.get("loss"));

            BigDecimal gainPrice;
            BigDecimal lossPrice;
            if (allCandles.get(i - 1).getClose().compareTo(allCandles.get(i).getClose()) < 0) {
                gainPrice = allCandles.get(i).getClose().subtract(allCandles.get(i - 1).getClose());
                lossPrice = new BigDecimal(0);
            }
            else if (allCandles.get(i).getClose().compareTo(allCandles.get(i - 1).getClose()) < 0) {
                gainPrice = new BigDecimal(0);
                lossPrice = allCandles.get(i - 1).getClose().subtract(allCandles.get(i).getClose());
            }
            else {
                gainPrice = new BigDecimal(0);
                lossPrice = new BigDecimal(0);
            }

            moveParamContainer(container, gainPrice, lossPrice);

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

            RSI.add(current_RSI);
        }
        parameter_saver.put("RSI", RSI);

        return parameter_saver;
    }
}
