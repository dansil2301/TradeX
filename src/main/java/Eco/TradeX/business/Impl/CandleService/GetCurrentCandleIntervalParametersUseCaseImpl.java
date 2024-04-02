package Eco.TradeX.business.Impl.CandleService;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCurrentCandleIntervalParametersUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;

import static Eco.TradeX.business.utils.CandleUtils.CandlesIntervalChecker.isCandleSwitchedToNextInterval;

@Service
public class GetCurrentCandleIntervalParametersUseCaseImpl implements GetCurrentCandleIntervalParametersUseCase {
    private CandleData currentCandle;
    private CandleData currentCandleTimeFrame;
    private CandleInterval candleInterval;
    private final ClientAPIRepository clientAPIRepository;

    GetCurrentCandleIntervalParametersUseCaseImpl(ClientAPIRepository clientAPIRepository) {
        this.clientAPIRepository = clientAPIRepository;
    }

    public void setLastCandleAndInterval(String figi, CandleInterval interval) {
        Instant now = Instant.now();
        int periodThatIsStableToFetchLastCandle = 5;
        candleInterval = interval;
        currentCandle = clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(now, figi, interval, periodThatIsStableToFetchLastCandle)
                .get(periodThatIsStableToFetchLastCandle - 1);
    }

    public CandleData getLastCandle() {
        return currentCandle;
    }

    public void updateLastCandle(CandleData streamCandle) {
        if (isCandleSwitchedToNextInterval(streamCandle, currentCandle, candleInterval)) {
            currentCandle = streamCandle;
        }
        else {
            currentCandle.setClose(streamCandle.getClose());
            currentCandle.setHigh(streamCandle.getHigh());
            currentCandle.setLow(streamCandle.getLow());
            setTotalVolume(streamCandle);
        }
    }

    private void setTotalVolume(CandleData streamCandle) {
        if (currentCandleTimeFrame == null) {
            currentCandleTimeFrame = streamCandle;
        }
        else {
            if (isCandleSwitchedToNextInterval(streamCandle, currentCandleTimeFrame, CandleInterval.CANDLE_INTERVAL_1_MIN)) {
                currentCandle.setVolume(currentCandle.getVolume() + streamCandle.getVolume());
            }
            else{
                Long volumeToAdd = streamCandle.getVolume() - currentCandleTimeFrame.getVolume();
                currentCandle.setVolume(currentCandle.getVolume() + volumeToAdd);
            }
            currentCandleTimeFrame = streamCandle;
        }
    }
}
