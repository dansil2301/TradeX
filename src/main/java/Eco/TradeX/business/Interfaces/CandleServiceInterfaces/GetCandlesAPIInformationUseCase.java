package Eco.TradeX.business.Interfaces.CandleServiceInterfaces;

import Eco.TradeX.domain.CandleData;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

public interface GetCandlesAPIInformationUseCase {
    List<CandleData> getHistoricalCandlesAPI(Instant _from, Instant _to, String figi, CandleInterval interval);
    List<CandleData> getFixedLengthHistoricalCandlesFromAPI(Instant _from, String figi, CandleInterval interval, int candlesLength);
    List<CandleData> getFixedLengthHistoricalCandlesFromAPIFuture(Instant _from, String figi, CandleInterval interval, int candlesLength);
    void candleStreaming(String figi, StreamProcessor<MarketDataResponse> processor);
}
