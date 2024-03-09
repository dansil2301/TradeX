package Eco.TradeX.business.Impl.StrategiesService;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.StrategiesServiceinterface.ParameterContainer;
import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.StrategiesServiceinterface.StrategyFactoryUseCase;
import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.StrategiesServiceinterface.StrategyUseCase;
import Eco.TradeX.business.exceptions.CandlesExceptions;
import Eco.TradeX.business.exceptions.StrategyExceptions;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.persistence.ClientAPIRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.*;

import static Eco.TradeX.business.utils.PackCandleStrategyParams.packCandles;

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

        if (toCalcStrategies.size() == getStrategiesNames().size())
        { return toCalcStrategies; }
        else { throw new StrategyExceptions("Some strategy names from the list don't exist"); }
    }

    private void initializeStrategiesExtraCandles(List<StrategyUseCase> strategies, Instant from, String figi, CandleInterval interval) {
        int biggestExtraCandlesLenNeeded = 0;
        for (var strategy : strategies) {
            if (strategy.getExtraCandlesNeeded() > biggestExtraCandlesLenNeeded) {
                biggestExtraCandlesLenNeeded = strategy.getExtraCandlesNeeded();
            }
        }

        List<CandleData> extraCandles = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(from, figi,
                interval, biggestExtraCandlesLenNeeded);

        for (var strategy : strategies) {
            var subLst = extraCandles.subList(Math.max(extraCandles.size() - strategy.getExtraCandlesNeeded(), 0), extraCandles.size());
            strategy.initializeExtraCandlesThroughFactory(subLst);
        }
    }

    @Override
    public List<CandleStrategiesParams> getCandlesStrategiesParameters(List<String> strategyNames,
                                                                       List<CandleData> candles,
                                                                       Instant from, String figi,
                                                                       CandleInterval interval) {
        if (candles == null) {
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
