package Eco.TradeX.business.Impl.CandleService;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.domain.CandleData;

import Eco.TradeX.persistence.Impl.CandleRepository.tinkoff.ClientTinkoffCandleStream;
import org.springframework.stereotype.Service;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

@Service
public class GetCandlesAPIInformationUseCaseImpl implements GetCandlesAPIInformationUseCase {
    private final ClientAPIRepository clientAPIRepository;
    private final ClientTinkoffCandleStream candleStream;

    public GetCandlesAPIInformationUseCaseImpl(ClientTinkoffCandleStream candleStream, ClientAPIRepository clientAPIRepository) {
        this.clientAPIRepository = clientAPIRepository;
        this.candleStream = candleStream;
    }

    @Override
    public List<CandleData> getHistoricalCandlesAPI(Instant _from, Instant _to, String figi, CandleInterval interval) {
        return clientAPIRepository.getHistoricalCandles(_from, _to, figi, interval);
    }

    @Override
    public List<CandleData> getFixedLengthHistoricalCandlesFromAPI(Instant _from, String figi, CandleInterval interval, int candlesLength) {
        return clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(_from, figi, interval, candlesLength);
    }

    @Override
    public List<CandleData> getFixedLengthHistoricalCandlesFromAPIFuture(Instant _from, String figi, CandleInterval interval, int candlesLength) {
        return clientAPIRepository.getExtraCandlesFromCertainTimeToFuture(_from, figi, interval, candlesLength);
    }

    @Override
    public void candleStreaming(String figi, StreamProcessor<MarketDataResponse> processor) {
        candleStream.getStreamServiceCandle(figi, processor);
    }
}
