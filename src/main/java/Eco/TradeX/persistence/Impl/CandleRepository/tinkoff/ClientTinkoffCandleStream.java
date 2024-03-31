package Eco.TradeX.persistence.Impl.CandleRepository.tinkoff;

import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientCandleStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ClientTinkoffCandleStream implements ClientCandleStream {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAPIRepository.class);
    private final InvestApi investApi;
    private Candle receivedCandle = null;

    public ClientTinkoffCandleStream(InvestApi investApi){
        this.investApi = investApi;
    }

    @Override
    public Candle getStreamServiceCandle(String figi) {
        investApi.getMarketDataStreamService().newStream("candles_stream", processor, onErrorCallback).subscribeCandles(Collections.singletonList(figi));
        return receivedCandle;
    }

    Consumer<Throwable> onErrorCallback = error -> LOGGER.error(error.toString());

    StreamProcessor<MarketDataResponse> processor = response -> {
        var subscribeResult = response.getSubscribeCandlesResponse().getCandlesSubscriptionsList().stream()
                .collect(Collectors.groupingBy(el -> el.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS), Collectors.counting()));

        LOGGER.warn(subscribeResult.toString());
    };
}
