package Eco.TradeX.business.Impl;

import Eco.TradeX.business.GetCandlesAPIInformationUseCase;
import Eco.TradeX.domain.CandleData;

import org.springframework.stereotype.Service;
import Eco.TradeX.persistence.ClientAPIRepository;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

@Service
public class GetCandlesAPIInformationUseCaseImpl implements GetCandlesAPIInformationUseCase {
    private final ClientAPIRepository clientAPIRepository;

    public GetCandlesAPIInformationUseCaseImpl(ClientAPIRepository clientAPIRepository) {
        this.clientAPIRepository = clientAPIRepository;
    }

    @Override
    public List<CandleData> getHistoricalCandlesAPI(Instant _from, Instant _to, String figi, CandleInterval interval) {
        return clientAPIRepository.getHistoricalCandles(_from, _to, figi, interval);
    }

    @Override
    public List<CandleData> getExtraHistoricalCandlesFromCertainTimeAPI(Instant _from, String figi, CandleInterval interval, int extraCandlesNeeded) {
        return clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(_from, figi, interval, extraCandlesNeeded);
    }
}
