package Eco.TradeX.business.Interfaces.CandleServiceInterfaces;

import Eco.TradeX.domain.CandleData;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

public interface GetCurrentCandleIntervalParametersUseCase {
    void setLastCandleAndInterval(String figi, CandleInterval interval);
    CandleData getLastCandle();
    void updateLastCandle(CandleData streamCandle);
}
