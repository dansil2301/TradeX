package Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces;

import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

public interface ClientCandleStream {
    Candle getStreamServiceCandle(String figi);
}
