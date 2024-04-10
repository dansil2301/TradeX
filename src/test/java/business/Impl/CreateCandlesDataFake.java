package business.Impl;

import Eco.TradeX.domain.CandleData;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CreateCandlesDataFake {
    private static final Random random = new Random();

    private BigDecimal generateRandomBigDecimal() {
        return BigDecimal.valueOf(random.nextDouble() * 100);
    }

    private int generateRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private long generateRandomLong(long min, long max) {
        return min + (long) (random.nextDouble() * (max - min));
    }

    public List<CandleData> createCandles(int amountToGenerate) {
        List<CandleData> candlesFake = new ArrayList<>();

        for (int i = 0; i < amountToGenerate; i++) {
            CandleData candleFake = CandleData.builder()
                    .low(generateRandomBigDecimal())
                    .high(generateRandomBigDecimal())
                    .open(generateRandomBigDecimal())
                    .close(generateRandomBigDecimal())
                    .time(LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant())
                    .volume(450)
                    .build();
            candlesFake.add(candleFake);
        }

        return candlesFake;
    }

    public List<HistoricCandle> createHistoricalCandles(int amountToGenerate) {
        List<HistoricCandle> historicCandlesFake = new ArrayList<>();

        Instant instant = Instant.parse("2023-01-02T00:00:00Z");
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();

        for (int i = 0; i < amountToGenerate; i++) {
            HistoricCandle candleFake = HistoricCandle.newBuilder()
                    .setClose(Quotation.newBuilder().setNano(generateRandomInt(10, 100)).setUnits(generateRandomLong(1, 100)).build())
                    .setLow(Quotation.newBuilder().setNano(generateRandomInt(10, 100)).setUnits(generateRandomLong(1, 100)).build())
                    .setHigh(Quotation.newBuilder().setNano(generateRandomInt(10, 100)).setUnits(generateRandomLong(1, 100)).build())
                    .setOpen(Quotation.newBuilder().setNano(generateRandomInt(10, 100)).setUnits(generateRandomLong(1, 100)).build())
                    .setVolume(450)
                    .setTime(timestamp)
                    .build();
            historicCandlesFake.add(candleFake);
        }

        return historicCandlesFake;
    }

    public List<Candle> createOriginalCandles(int amountToGenerate) {
        List<Candle> historicCandlesFake = new ArrayList<>();

        Instant instant = Instant.parse("2023-01-02T00:00:00Z");
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();

        for (int i = 0; i < amountToGenerate; i++) {
             Candle candleFake = Candle.newBuilder()
                    .setClose(Quotation.newBuilder().setNano(generateRandomInt(10, 100)).setUnits(generateRandomLong(1, 100)).build())
                    .setLow(Quotation.newBuilder().setNano(generateRandomInt(10, 100)).setUnits(generateRandomLong(1, 100)).build())
                    .setHigh(Quotation.newBuilder().setNano(generateRandomInt(10, 100)).setUnits(generateRandomLong(1, 100)).build())
                    .setOpen(Quotation.newBuilder().setNano(generateRandomInt(10, 100)).setUnits(generateRandomLong(1, 100)).build())
                    .setVolume(450)
                    .setTime(timestamp)
                    .build();
            historicCandlesFake.add(candleFake);
        }

        return historicCandlesFake;
    }
}

