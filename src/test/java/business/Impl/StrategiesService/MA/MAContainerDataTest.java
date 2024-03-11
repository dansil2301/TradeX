package business.Impl.StrategiesService.MA;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.StrategiesService.MA.MAContainerData;
import Eco.TradeX.business.Impl.StrategiesService.RSI.RSIContainerData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TradeXApplication.class)
class MAContainerDataTest {

    @Test
    void moveByOneTest1to2() {
        List<BigDecimal> candle = new ArrayList<>();
        candle.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));

        var rsiContainer = MAContainerData.builder()
                .candlesCloseLong(candle)
                .build();

        rsiContainer.moveByOne(new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP));

        assertEquals(new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP), rsiContainer.getCandlesCloseLong().get(0));
    }

    @Test
    void moveByOneTest1to3() {
        List<BigDecimal> candle = new ArrayList<>();
        candle.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));

        var rsiContainer = MAContainerData.builder()
                .candlesCloseLong(candle)
                .build();

        rsiContainer.moveByOne(new BigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP));

        assertEquals(new BigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP), rsiContainer.getCandlesCloseLong().get(0));
    }
}