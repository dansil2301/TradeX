package Eco.TradeX.business.Impl.CandleService;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.domain.CandleData;

import org.springframework.stereotype.Service;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
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
    public List<CandleData> getFixedLengthHistoricalCandlesFromAPI(Instant _from, String figi, CandleInterval interval, int candlesLength) {
        return clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(_from, figi, interval, candlesLength);
    }
}
