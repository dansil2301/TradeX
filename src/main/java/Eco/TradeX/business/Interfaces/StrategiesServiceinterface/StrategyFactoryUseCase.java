package Eco.TradeX.business.Interfaces.StrategiesServiceinterface;

import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

public interface StrategyFactoryUseCase {
    List<String> getStrategiesNames();
    List<StrategyUseCase> getStrategies(List<String> strategyNames);
    List<CandleStrategiesParams> getCandlesStrategiesParameters(List<String> strategyNames,
                                                                List<CandleData> candles,
                                                                Instant from, String figi, CandleInterval interval);
}
