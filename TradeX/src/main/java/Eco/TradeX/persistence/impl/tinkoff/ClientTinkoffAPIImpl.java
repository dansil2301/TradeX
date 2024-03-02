package Eco.TradeX.persistence.impl.tinkoff;

import Eco.TradeX.business.utils.CandleIntervalConverter;
import Eco.TradeX.domain.CandleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import Eco.TradeX.persistence.ClientAPIRepository;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;

@Repository
public class ClientTinkoffAPIImpl implements ClientAPIRepository {
    // should make it more flexible
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAPIRepository.class);
    private static final String figi = "BBG004730N88";
    private final InvestApi investApi;

    public ClientTinkoffAPIImpl(TokenManagerTinkoffImpl tokenManager){
        this.investApi = InvestApi.createReadonly(tokenManager.readTokenLocally());
    }

    @Override
    public List<CandleData> getHistoricalCandles(Instant _from, Instant _to, String figi, CandleInterval interval) {
        try {
            List<HistoricCandle> candles = investApi.getMarketDataService()
                    .getCandlesSync(this.figi, _from, _to, interval);

            if (candles.isEmpty()) {
                LOGGER.warn("Empty response for candles.");
                return null;
            }

            return candles.stream().map(originalCandle -> {
                return CandleData.builder()
                        .open(quotationToBigDecimal(originalCandle.getOpen()))
                        .close(quotationToBigDecimal(originalCandle.getClose()))
                        .high(quotationToBigDecimal(originalCandle.getHigh()))
                        .low(quotationToBigDecimal(originalCandle.getLow()))
                        .volume(originalCandle.getVolume())
                        .time(Instant.ofEpochSecond(originalCandle.getTime().getSeconds(), originalCandle.getTime().getNanos()))
                        .build();
            }).toList();
        } catch (Exception e) {
            LOGGER.error("Error fetching historical candles: " + e.getLocalizedMessage());
            throw new RuntimeException("Error fetching historical candles");
        }
    }


    public List<CandleData> getExtraHistoricalCandlesFromCertainTime(Instant _from, String figi, CandleInterval interval, int extraCandlesNeeded){
        Instant from = _from;
        List<CandleData> candles = Collections.emptyList();

        // todo think about stop condition in case of going out of bounds
        while (candles.size() < extraCandlesNeeded) {
            int newPeriod = extraCandlesNeeded - candles.size();
            from = from.minusSeconds((long) CandleIntervalConverter.toSeconds(interval) * newPeriod);
            candles = getHistoricalCandles(from, _from, figi, interval);
            candles = (candles == null) ? Collections.emptyList() : candles;
        }

        if (extraCandlesNeeded != candles.size()) {
            candles = candles.subList(Math.max(candles.size() - extraCandlesNeeded, 0), candles.size());
        }

        return candles;
    }

    @Override
    public Candle getStreamServiceCandle(String figi, CandleInterval interval) {

        return null;
    }
}
