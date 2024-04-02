package Eco.TradeX.persistence.Impl.CandleRepository.tinkoff;

import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientCandleStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.util.Collections;
import java.util.function.Consumer;

import static Eco.TradeX.business.utils.CandleUtils.ConvertToLocalCandleEntity.convertToCandleData;

@Repository
public class ClientTinkoffCandleStream implements ClientCandleStream {
    private final InvestApi investApi;
    private CandleData receivedCandle = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientTinkoffCandleStream.class);

    public ClientTinkoffCandleStream(InvestApi investApi){
        this.investApi = investApi;
    }

    @Override
    public void getStreamServiceCandle(String figi, StreamProcessor<MarketDataResponse> processor) {
        Consumer<Throwable> onErrorCallback = error -> LOGGER.info("Tinkoff data stream start error: " + error);
        investApi.getMarketDataStreamService().newStream("candles_stream", processor, onErrorCallback).subscribeCandles(Collections.singletonList(figi));
        LOGGER.info("Tinkoff data stream started");
    }
}
