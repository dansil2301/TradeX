package persistence.impl.tinkoff;

import org.springframework.stereotype.Repository;
import persistence.ClientAPIRepository;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.time.Instant;
import java.util.List;

@Repository
public class ClientTinkoffAPIImpl implements ClientAPIRepository {


    @Override
    public List<HistoricCandle> getHistoricalCandles(String figi, Instant _from, Instant _to) {
        return null;
    }

    @Override
    public Candle getStreamServiceCandle(String figi, CandleInterval interval) {
        return null;
    }
}
