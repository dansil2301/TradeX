package Eco.TradeX.persistence.Utils;

import Eco.TradeX.domain.CandleData;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.time.Instant;
import java.util.List;

import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;

public class ConvertToLocalCandleEntity {
    public static CandleData convertToCandleData(HistoricCandle originalCandle) {
        return CandleData.builder()
                .open(quotationToBigDecimal(originalCandle.getOpen()))
                .close(quotationToBigDecimal(originalCandle.getClose()))
                .high(quotationToBigDecimal(originalCandle.getHigh()))
                .low(quotationToBigDecimal(originalCandle.getLow()))
                .volume(originalCandle.getVolume())
                .time(Instant.ofEpochSecond(originalCandle.getTime().getSeconds(), originalCandle.getTime().getNanos()))
                .build();
    }

    public static List<CandleData> convertToCandlesData(List<HistoricCandle> originalCandles) {
        return originalCandles.stream().map(originalCandle -> CandleData.builder()
                .open(quotationToBigDecimal(originalCandle.getOpen()))
                .close(quotationToBigDecimal(originalCandle.getClose()))
                .high(quotationToBigDecimal(originalCandle.getHigh()))
                .low(quotationToBigDecimal(originalCandle.getLow()))
                .volume(originalCandle.getVolume())
                .time(Instant.ofEpochSecond(originalCandle.getTime().getSeconds(), originalCandle.getTime().getNanos()))
                .build()).toList();
    }
}
