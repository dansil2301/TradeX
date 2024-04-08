package business.Impl;

import Eco.TradeX.domain.CandleData;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import com.google.protobuf.TimestampOrBuilder;
import lombok.Data;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.HistoricCandleOrBuilder;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.contract.v1.QuotationOrBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateCandlesDataFake {
    private static final Random random = new Random();

    private BigDecimal generateRandomBigDecimal() {
        return BigDecimal.valueOf(random.nextDouble() * 100);
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
        return null;
    }
}

