package Eco.TradeX.business.Interfaces.CandleServiceInterfaces;

import Eco.TradeX.domain.CandleData;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

public interface GetCandlesAPIInformationUseCase {
    List<CandleData> getHistoricalCandlesAPI(Instant _from, Instant _to, String figi, CandleInterval interval);
}
