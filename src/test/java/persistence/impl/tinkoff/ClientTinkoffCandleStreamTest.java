package persistence.impl.tinkoff;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.persistence.Impl.CandleRepository.tinkoff.ClientTinkoffCandleStream;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientCandleStream;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.MarketDataStreamService;
import ru.tinkoff.piapi.core.stream.MarketDataSubscriptionService;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TradeXApplication.class)
class ClientTinkoffCandleStreamTest {
    @Mock
    private InvestApi investApi;

    @InjectMocks
    private ClientTinkoffCandleStream clientTinkoffCandleStream;

    @Test
    void testGetStreamServiceCandle() {
        String figi = "your_figi";
        StreamProcessor<MarketDataResponse> processor = mock(StreamProcessor.class);
        MarketDataStreamService marketDataStreamService = mock(MarketDataStreamService.class);
        MarketDataSubscriptionService marketDataSubscriptionService = mock(MarketDataSubscriptionService.class);

        when(investApi.getMarketDataStreamService()).thenReturn(marketDataStreamService);

        when(investApi.getMarketDataStreamService().newStream(
                eq("candles_stream"),
                any(StreamProcessor.class),
                any(Consumer.class)
        )).thenReturn(marketDataSubscriptionService);

        doNothing().when(marketDataSubscriptionService).subscribeCandles(Collections.singletonList(figi));

        clientTinkoffCandleStream.getStreamServiceCandle(figi, processor);

        verify(marketDataSubscriptionService).subscribeCandles(Collections.singletonList(figi));
    }
}