package business.Impl.StrategiesService.RSI;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.StrategiesService.RSI.RSIContainerData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TradeXApplication.class)
class RSIContainerDataTest {

    @Test
    void moveByOneTest11to22() {
        List<BigDecimal> gain = new ArrayList<>();
        gain.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));
        List<BigDecimal> loss = new ArrayList<>();
        loss.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));

        var rsiContainer = RSIContainerData.builder()
                .loss(loss)
                .gain(gain)
                .build();

        rsiContainer.moveByOne(new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP),
                new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP));

        assertEquals(new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP), rsiContainer.getGain().get(0));
        assertEquals(new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP), rsiContainer.getLoss().get(0));
    }

    @Test
    void moveByOneTest11to23() {
        List<BigDecimal> gain = new ArrayList<>();
        gain.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));
        List<BigDecimal> loss = new ArrayList<>();
        loss.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));

        var rsiContainer = RSIContainerData.builder()
                .loss(loss)
                .gain(gain)
                .build();

        rsiContainer.moveByOne(new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP),
                new BigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP));

        assertEquals(new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP), rsiContainer.getGain().get(0));
        assertEquals(new BigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP), rsiContainer.getLoss().get(0));
    }
}