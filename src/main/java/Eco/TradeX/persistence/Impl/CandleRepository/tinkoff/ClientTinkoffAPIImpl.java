package Eco.TradeX.persistence.Impl.CandleRepository.tinkoff;

import Eco.TradeX.business.exceptions.CandlesExceptions;
import Eco.TradeX.domain.CandleData;
import com.google.protobuf.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.time.*;
import java.util.*;
import java.util.function.Consumer;

import static Eco.TradeX.business.utils.CandleUtils.CandleIntervalConverter.toMaximumFetchPeriodInSeconds;
import static Eco.TradeX.business.utils.CandleUtils.CandleIntervalConverter.toSeconds;
import static Eco.TradeX.business.utils.CandleUtils.ConvertToLocalCandleEntity.convertToCandlesData;

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

            return convertToCandlesData(candles);
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

    public List<CandleData> getExtraHistoricalCandlesFromCertainTime(Instant _from, String figi, CandleInterval interval, int extraCandlesNeeded) {
        Instant from = _from;
        Instant to = null;
        List<CandleData> candles = new ArrayList<>();
        Instant stopDate = getLastAvailableDate(figi);

        while (candles.size() < extraCandlesNeeded) {
            to = to == null ? from : from.minusSeconds(toSeconds(interval));
            from = from.minusSeconds(toMaximumFetchPeriodInSeconds(interval));

            candles.addAll(0, getHistoricalCandles(from, to, figi, interval));
            candles = candles == null ? new ArrayList<>() : candles;

            if (from.compareTo(stopDate) < 0) {
                break;
            }
        }

        if (extraCandlesNeeded != candles.size()) {
            candles = candles.subList(Math.max(candles.size() - extraCandlesNeeded, 0), candles.size());
        }

        return candles;
    }

    public List<CandleData> getExtraCandlesFromCertainTimeToFuture(Instant _from, String figi, CandleInterval interval, int extraCandlesNeeded) {
        Instant from = null;
        Instant to = _from;
        List<CandleData> candles = new ArrayList<>();
        Instant stopDate = Instant.now();

        while (candles.size() < extraCandlesNeeded) {
            from = to.plusSeconds(toSeconds(interval));
            to = to.plusSeconds(toMaximumFetchPeriodInSeconds(interval));

            candles.addAll(getHistoricalCandles(from, to, figi, interval));
            candles = candles == null ? new ArrayList<>() : candles;

            if (to.compareTo(stopDate) > 0) {
                break;
            }
        }

        if (extraCandlesNeeded != candles.size()) {
            candles = candles.subList(0, extraCandlesNeeded);
        }

        return candles;
    }

    // todo build for future socket
    public Candle getStreamServiceCandle(String figi, CandleInterval interval) {
        investApi.getMarketDataStreamService().newStream("candles_stream", processor, onErrorCallback).subscribeCandles(Collections.singletonList(figi));

        return null;
    }

    Consumer<Throwable> onErrorCallback = error -> LOGGER.error(error.toString());

    StreamProcessor<MarketDataResponse> processor = response -> {

    };
}
