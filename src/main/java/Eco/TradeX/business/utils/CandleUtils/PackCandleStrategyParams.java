package Eco.TradeX.business.utils.CandleUtils;

import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.ParameterContainer;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.domain.StrategyParams.StrategyNameParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackCandleStrategyParams {
    static public List<CandleStrategiesParams> packCandles(List<CandleData> candles, Map<String, List<ParameterContainer>> allParameters) {
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
