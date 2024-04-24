package Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces;

import Eco.TradeX.domain.Requests.GetSocketMarketStreaming;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

public interface StrategyStreamingHelperUseCase {
    CandleStrategiesParams CalculateCandleParams(GetSocketMarketStreaming request, MarketDataResponse response);
    void initializeStreamingServices(StreamProcessor<MarketDataResponse> processor, GetSocketMarketStreaming request);
}
