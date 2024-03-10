package Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces;

import Eco.TradeX.domain.CandleData;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

public interface StrategyUseCase {
    String getStrategyName();
    int getExtraCandlesNeeded();
    void initializeContainerForCandleLiveStreaming(String figi, CandleInterval interval);
    ParameterContainer calculateParametersForCandle(CandleData candle);
    void initializeExtraCandlesThroughFactory(List<CandleData> extraCandles);
    List<ParameterContainer> getStrategyParametersForCandles(List<CandleData> candles, Instant from, String figi, CandleInterval interval);
}
