package Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces;

import Eco.TradeX.domain.CandleData;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.util.function.Consumer;

public interface ClientCandleStream {
    void getStreamServiceCandle(String figi, StreamProcessor<MarketDataResponse> processor);
}
