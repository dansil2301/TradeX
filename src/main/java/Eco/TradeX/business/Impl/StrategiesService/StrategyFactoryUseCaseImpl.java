package Eco.TradeX.business.Impl.StrategiesService;

import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.ParameterContainer;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyFactoryUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyUseCase;
import Eco.TradeX.business.exceptions.CandlesExceptions;
import Eco.TradeX.business.exceptions.StrategyExceptions;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.domain.StrategyParams.StrategyNameParameter;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.*;

import static Eco.TradeX.business.utils.CandleUtils.PackCandleStrategyParams.packCandles;

@Service
@AllArgsConstructor
public class StrategyFactoryUseCaseImpl implements StrategyFactoryUseCase {
    private final List<StrategyUseCase> strategies;
    private final ClientAPIRepository clientAPIRepository;

    @Override
    public List<String> getStrategiesNames() {
        List<String> names = new ArrayList<>();

        for (var strategy : strategies) {
            names.add(strategy.getStrategyName());
        }

        return names;
    }

    @Override
    public List<StrategyUseCase> getStrategies(List<String> strategyNames) {
        List<StrategyUseCase> toCalcStrategies = new ArrayList<>();

        for (var strategy : strategies) {
            if (strategyNames.contains(strategy.getStrategyName())) {
                toCalcStrategies.add(strategy);
            }
        }

        if (toCalcStrategies.size() == strategyNames.size())
        { return toCalcStrategies; }
        else { throw new StrategyExceptions("Some strategy names from the list don't exist"); }
    }

    @Override
    public void initializeContainerForCandleLiveStreaming(List<String> strategyNames, String figi, CandleInterval interval) {
        Instant now = Instant.now();
        List<StrategyUseCase> strategies = getStrategies(strategyNames);
        initializeStrategiesExtraCandles(strategies, now, figi, interval);
    }

    @Override
    public CandleStrategiesParams calculateParametersForCandle(CandleData candle, List<String> strategyNames, CandleInterval interval) {
        List<StrategyUseCase> strategies = getStrategies(strategyNames);
        List<StrategyNameParameter> strategyNameParameters = new ArrayList<>();

        for (var strategy : strategies) {
            strategyNameParameters.add(StrategyNameParameter.builder()
                            .strategyName(strategy.getStrategyName())
                            .parameters(strategy.calculateParametersForCandle(candle, interval))
                            .build());
        }

        return CandleStrategiesParams.builder()
                .candle(candle)
                .strategyNameParameters(strategyNameParameters)
                .build();
    }

    private void initializeStrategiesExtraCandles(List<StrategyUseCase> strategies, Instant from, String figi, CandleInterval interval) {
        int biggestExtraCandlesLenNeeded = getBiggestExtraCandlesLenNeeded(strategies);

        List<CandleData> extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(from, figi,
                interval, biggestExtraCandlesLenNeeded);

        for (var strategy : strategies) {
            var subLst = extraCandles.subList(Math.max(extraCandles.size() - strategy.getExtraCandlesNeeded(), 0), extraCandles.size());
            strategy.initializeExtraCandlesThroughFactory(subLst);
        }
    }

    private int getBiggestExtraCandlesLenNeeded(List<StrategyUseCase> strategies) {
        int biggestExtraCandlesLenNeeded = 0;
        for (var strategy : strategies) {
            if (strategy.getExtraCandlesNeeded() > biggestExtraCandlesLenNeeded) {
                biggestExtraCandlesLenNeeded = strategy.getExtraCandlesNeeded();
            }
        }
        return biggestExtraCandlesLenNeeded;
    }

    @Override
    public List<CandleStrategiesParams> getCandlesStrategiesParameters(List<String> strategyNames,
                                                                       List<CandleData> candles,
                                                                       Instant from, String figi,
                                                                       CandleInterval interval) {
        if (candles == null || candles.isEmpty()) {
            throw new CandlesExceptions("No candles found for this period");
        }

        List<StrategyUseCase> strategies = getStrategies(strategyNames);
        initializeStrategiesExtraCandles(strategies, from, figi, interval);
        Map<String, List<ParameterContainer>> allParameters = new HashMap<>();

        // get all needed parameters
        for (var strategy : strategies) {
            allParameters.put(strategy.getStrategyName(), strategy.getStrategyParametersForCandles(candles,
                    from, figi, interval));
        }

        return packCandles(candles, allParameters);
    }
}
