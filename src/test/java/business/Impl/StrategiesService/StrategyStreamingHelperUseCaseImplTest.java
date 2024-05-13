package business.Impl.StrategiesService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.StrategiesService.StrategyStreamingHelperUseCaseImpl;
import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCurrentCandleIntervalParametersUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyFactoryUseCase;
import Eco.TradeX.domain.Requests.GetSocketMarketStreaming;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = TradeXApplication.class)
class StrategyStreamingHelperUseCaseImplTest {
    @Mock
    GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;

    @Mock
    StrategyFactoryUseCase strategyFactoryUseCase;

    @Mock
    GetCurrentCandleIntervalParametersUseCase getCurrentCandleIntervalParametersUseCase;

    @InjectMocks
    StrategyStreamingHelperUseCaseImpl strategyHelper;

    @Captor
    ArgumentCaptor<StreamProcessor<MarketDataResponse>> processorCaptor;

//    @Test
//    void testInitializeStreamingServices() {
//        GetSocketMarketStreaming request = GetSocketMarketStreaming.builder().build();
//
//        MockitoAnnotations.openMocks(this);
//
//        doNothing().when(getCurrentCandleIntervalParametersUseCase).setLastCandleAndInterval(any(), any());
//        doNothing().when(strategyFactoryUseCase).initializeContainerForCandleLiveStreaming(any(), any(), any());
//        doNothing().when(getCandlesAPIInformationUseCase).candleStreaming(eq(request.getFigi()), any());
//
//        strategyHelper.initializeStreamingServices(processorCaptor.capture(), request);
//
//        // if no errors then fine
//        assertEquals(true, true);
//    }
}