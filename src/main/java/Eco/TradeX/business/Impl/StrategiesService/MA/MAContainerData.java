package Eco.TradeX.business.Impl.StrategiesService.MA;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class MAContainerData {
    private List<BigDecimal> candlesCloseLong;

    public void moveByOne(BigDecimal newCloseCandle) {
        List<BigDecimal> newCandlesCloseLong = new ArrayList<>(candlesCloseLong);

        newCandlesCloseLong.add(newCloseCandle);
        newCandlesCloseLong.remove(0);

        candlesCloseLong = newCandlesCloseLong;
    }

    public void changeLast(BigDecimal newCloseCandle) {
        List<BigDecimal> newCandlesCloseLong = new ArrayList<>(candlesCloseLong);

        int lastIndex = candlesCloseLong.size() - 1;

        newCandlesCloseLong.set(lastIndex, newCloseCandle);
        candlesCloseLong = newCandlesCloseLong;
    }
}
