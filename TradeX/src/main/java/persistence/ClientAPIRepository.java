package persistence;

import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.time.Instant;
import java.util.List;

public interface ClientAPIRepository {
    <T> List<T> getHistoricalCandles(Instant _from, Instant _to, String figi, CandleInterval interval);
    Candle getStreamServiceCandle(String figi, CandleInterval interval);
}
