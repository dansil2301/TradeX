package business.utils.CandleUtils;

import Eco.TradeX.business.utils.CandleUtils.ConvertToLocalCandleEntity;
import Eco.TradeX.domain.CandleData;
import business.Impl.CreateCandlesDataFake;
import org.junit.jupiter.api.Test;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConvertToLocalCandleEntityTest {
    private CreateCandlesDataFake createCandlesDataFake = new CreateCandlesDataFake();

    @Test
    void convertToCandleDataHistorical() {
        CandleData candle = ConvertToLocalCandleEntity.convertToCandleData(createCandlesDataFake.createHistoricalCandles(1).get(0));
        assertNotEquals(null, candle);
    }

    @Test
    void convertToCandleDataOriginal() {
        CandleData candle = ConvertToLocalCandleEntity.convertToCandleData(createCandlesDataFake.createOriginalCandles(1).get(0));
        assertNotEquals(null, candle);
    }

    @Test
    void convertToCandlesData() {
        List<CandleData> candles = ConvertToLocalCandleEntity.convertToCandlesData(createCandlesDataFake.createHistoricalCandles(10));
        assertEquals(10, candles.size());
    }
}