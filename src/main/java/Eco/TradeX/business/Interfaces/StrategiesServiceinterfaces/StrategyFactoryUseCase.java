package Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces;

import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

public interface StrategyFactoryUseCase {
    List<String> getStrategiesNames();
    List<StrategyUseCase> getStrategies(List<String> strategyNames);
    void initializeContainerForCandleLiveStreaming(List<String> strategyNames, String figi, CandleInterval interval);
    void initializeContainers(List<String> strategyNames, String figi, CandleInterval interval, Instant from);
    CandleStrategiesParams calculateParametersForCandle(CandleData candle, List<String> strategyNames, CandleInterval interval);
    List<CandleStrategiesParams> getCandlesStrategiesParameters(List<String> strategyNames,
                                                                List<CandleData> candles,
                                                                Instant from, String figi, CandleInterval interval);
}
