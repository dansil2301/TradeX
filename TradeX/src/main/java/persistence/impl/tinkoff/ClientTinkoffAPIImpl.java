package persistence.impl.tinkoff;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import persistence.ClientAPIRepository;
import persistence.TokenManagerRepository;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.util.List;

@Repository
public class ClientTinkoffAPIImpl implements ClientAPIRepository {
    // should make it more flexible
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAPIRepository.class);
    private static final String figi = "BBG004730N88";
    private final InvestApi investApi;

    @Autowired
    public ClientTinkoffAPIImpl(TokenManagerTinkoffImpl tokenManager){
        this.investApi = InvestApi.createReadonly(tokenManager.readTokenLocally());
    }

    @Override
    public List<HistoricCandle> getHistoricalCandles(Instant _from, Instant _to, String figi, CandleInterval interval) {
        try {
            List<HistoricCandle> candles = investApi.getMarketDataService()
                    .getCandlesSync(this.figi, _from, _to, interval);

            if (candles.isEmpty()) {
                LOGGER.warn("Empty response for candles.");
                return null;
            }

            return candles;
        } catch (Exception e) {
            LOGGER.error("Error fetching historical candles: " + e.getLocalizedMessage());
            throw new RuntimeException("Error fetching historical candles");
        }
    }

    @Override
    public Candle getStreamServiceCandle(String figi, CandleInterval interval) {
        return null;
    }
}
