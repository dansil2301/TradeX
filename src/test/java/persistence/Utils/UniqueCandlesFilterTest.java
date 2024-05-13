package persistence.Utils;

import Eco.TradeX.domain.CandleData;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static Eco.TradeX.persistence.Utils.UniqueCandlesFilter.filterUniqueCandles;
import static org.junit.jupiter.api.Assertions.*;

class UniqueCandlesFilterTest {

    @Test
    void filterUniqueCandlesNoSkipTest() {
        List<CandleData> candleDataList = new ArrayList<>();

        candleDataList.add(CandleData.builder().time(Instant.now()).build());
        candleDataList.add(CandleData.builder().time(Instant.ofEpochMilli(10000000)).build());

        List<CandleData> noSkipCandles = filterUniqueCandles(candleDataList);

        assertEquals(candleDataList.size(), noSkipCandles.size());
    }

    @Test
    void filterUniqueCandlesSkipTest() {
        List<CandleData> candleDataList = new ArrayList<>();

        candleDataList.add(CandleData.builder().time(Instant.ofEpochMilli(10000000)).build());
        candleDataList.add(CandleData.builder().time(Instant.ofEpochMilli(10000000)).build());

        List<CandleData> noSkipCandles = filterUniqueCandles(candleDataList);

        assertEquals(1, noSkipCandles.size());
    }
}