package persistence;

import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.time.Instant;
import java.util.List;

public interface ClientAPIRepository {
    public List<HistoricCandle> getHistoricalCandles(String figi, Instant _from, Instant _to);
    public Candle getStreamServiceCandle(String figi, CandleInterval interval);
}
