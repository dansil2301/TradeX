package Eco.TradeX.business.Impl.StrategiesService;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCurrentCandleIntervalParametersUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyFactoryUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyStreamingHelperUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.Requests.GetSocketMarketStreaming;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import static Eco.TradeX.business.utils.CandleUtils.ConvertToLocalCandleEntity.convertToCandleData;

@Service
@AllArgsConstructor
public class StrategyStreamingHelperUseCaseImpl implements StrategyStreamingHelperUseCase {
    private final GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;
    private final StrategyFactoryUseCase strategyFactoryUseCase;
    private final GetCurrentCandleIntervalParametersUseCase getCurrentCandleIntervalParametersUseCase;

    public CandleStrategiesParams CalculateCandleParams(GetSocketMarketStreaming request, MarketDataResponse response) {
        CandleData candleData = convertToCandleData(response.getCandle());
        getCurrentCandleIntervalParametersUseCase.updateLastCandle(candleData);
        candleData = getCurrentCandleIntervalParametersUseCase.getLastCandle();
        CandleStrategiesParams candleStrategiesParams = strategyFactoryUseCase.calculateParametersForCandle(
                candleData,
                request.getStrategiesNames(),
                request.getInterval()
        );
        return candleStrategiesParams;
    }

    public void initializeStreamingServices(StreamProcessor<MarketDataResponse> processor, GetSocketMarketStreaming request) {
        getCurrentCandleIntervalParametersUseCase.setLastCandleAndInterval(request.getFigi(), request.getInterval());
        strategyFactoryUseCase.initializeContainerForCandleLiveStreaming(request.getStrategiesNames(), request.getFigi(), request.getInterval());
        getCandlesAPIInformationUseCase.candleStreaming(request.getFigi(), processor);
    }
}
