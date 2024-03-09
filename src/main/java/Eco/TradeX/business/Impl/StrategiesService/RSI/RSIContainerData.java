package Eco.TradeX.business.Impl.StrategiesService.RSI;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
public class RSIContainerData {
    private List<BigDecimal> gain;
    private List<BigDecimal> loss;

    public void moveByOne(BigDecimal newGain, BigDecimal newLoss) {
        gain.add(newGain);
        gain.remove(0);

        loss.add(newLoss);
        loss.remove(0);
    }
}
