package Eco.TradeX.business.Impl.Strategies;

import Eco.TradeX.business.ParameterContainer;
import Eco.TradeX.business.StrategyFactoryUseCase;
import Eco.TradeX.business.StrategyUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.domain.StrategyParams.StrategyNameParameter;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.*;

@Service
public class StrategyFactoryUseCaseImpl implements StrategyFactoryUseCase {
    private final List<StrategyUseCase> strategies;

    public StrategyFactoryUseCaseImpl(List<StrategyUseCase> strategies) {
        this.strategies = strategies;
    }

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
        else { throw new RuntimeException("No strategy with this name"); }
    }

    @Override
    public List<CandleStrategiesParams> getCandlesStrategiesParameters(List<String> strategyNames,
                                                                       List<CandleData> candles,
                                                                       Instant from, Instant to,
                                                                       String figi, CandleInterval interval) {
        List<StrategyUseCase> strategies = getStrategies(strategyNames);
        Map<String, List<ParameterContainer>> allParameters = new HashMap<>();

        // get all needed parameters and initialize
        for (var strategy : strategies) {
            allParameters.put(strategy.getStrategyName(), strategy.getStrategyParametersForCandles(candles,
                    from, to, figi, interval));
        }

        //pack into future jon format
        List<CandleStrategiesParams> candleStrategiesParams = new ArrayList<>();
        for (int candleIndex = 0; candleIndex < candles.size(); candleIndex++) {
            List<StrategyNameParameter> strategyNameParametersLst = new ArrayList<>();
            for (Map.Entry<String, List<ParameterContainer>> strategyNameParameters : allParameters.entrySet()) {
                String strategyName = strategyNameParameters.getKey();
                List<ParameterContainer> parameterContainers = strategyNameParameters.getValue();

                strategyNameParametersLst.add(StrategyNameParameter.builder()
                        .strategyName(strategyName)
                        .parameters(parameterContainers.get(candleIndex))
                        .build());
            }
            candleStrategiesParams.add(CandleStrategiesParams.builder()
                    .candle(candles.get(candleIndex))
                    .strategyNameParameters(strategyNameParametersLst)
                    .build());
        }

        return candleStrategiesParams;
    }
}
