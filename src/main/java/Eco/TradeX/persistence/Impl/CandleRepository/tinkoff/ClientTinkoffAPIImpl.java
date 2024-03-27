package Eco.TradeX.persistence.Impl.CandleRepository.tinkoff;

import Eco.TradeX.business.exceptions.CandlesExceptions;
import Eco.TradeX.domain.CandleData;
import com.google.protobuf.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InstrumentsService;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.*;
import java.util.*;

import static Eco.TradeX.business.utils.CandleUtils.CandleIntervalConverter.toMaximumFetchPeriod;
import static Eco.TradeX.business.utils.CandleUtils.CandleIntervalConverter.toSeconds;
import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;

@Repository
public class ClientTinkoffAPIImpl implements ClientAPIRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAPIRepository.class);
    private final InvestApi investApi;

    public ClientTinkoffAPIImpl(InvestApi investApi){
        this.investApi = investApi;
    }

    @Override
    public List<CandleData> getHistoricalCandles(Instant _from, Instant _to, String figi, CandleInterval interval) {
        try {
            List<HistoricCandle> candles = investApi.getMarketDataService()
                    .getCandlesSync(figi, _from, _to, interval);

            if (candles.isEmpty()) {
                LOGGER.warn("Empty response for candles.");
                return null;
            }

            return candles.stream().map(originalCandle -> CandleData.builder()
                    .open(quotationToBigDecimal(originalCandle.getOpen()))
                    .close(quotationToBigDecimal(originalCandle.getClose()))
                    .high(quotationToBigDecimal(originalCandle.getHigh()))
                    .low(quotationToBigDecimal(originalCandle.getLow()))
                    .volume(originalCandle.getVolume())
                    .time(Instant.ofEpochSecond(originalCandle.getTime().getSeconds(), originalCandle.getTime().getNanos()))
                    .build()).toList();
        } catch (Exception e) {
            LOGGER.error("Error fetching historical candles: " + e.getLocalizedMessage());
            throw new CandlesExceptions(e.getMessage());
        }
    }

    private String getUidByFigi(String figi) {
        Instrument instrument = investApi.getInstrumentsService().getInstrumentByFigiSync(figi);
        return instrument.getUid();
    }

    public Instant getLastAvailableDate(String figi) {
        try {
            Instrument instrument = investApi.getInstrumentsService().getInstrumentByFigiSync(figi);
            Timestamp first1MinCandleTimestamp = instrument.getFirst1MinCandleDate();
            return Instant.ofEpochSecond(first1MinCandleTimestamp.getSeconds(), first1MinCandleTimestamp.getNanos());
        }
        catch (Exception e) {
            throw new CandlesExceptions(e.getMessage());
        }
    }

    public List<CandleData> getExtraHistoricalCandlesFromCertainTime(Instant _from, String figi, CandleInterval interval, int extraCandlesNeeded){
        Instant from = _from;
        Instant to;
        List<CandleData> candles = new ArrayList<>();
        Instant stopDate = getLastAvailableDate(figi);

        while (candles.size() < extraCandlesNeeded) {
            to = from.minusSeconds(toSeconds(interval));
            from = from.minusSeconds(toMaximumFetchPeriod(interval));

            if (from.compareTo(stopDate) < 0) {
                break;
            }

            candles.addAll(0, getHistoricalCandles(from, to, figi, interval));
            candles = (candles == null) ? new ArrayList<>() : candles;
        }

        if (extraCandlesNeeded != candles.size()) {
            candles = candles.subList(Math.max(candles.size() - extraCandlesNeeded, 0), candles.size());
        }

        return candles;
    }

    // todo build for future socket
    @Override
    public Candle getStreamServiceCandle(String figi, CandleInterval interval) {

        return null;
    }
}
