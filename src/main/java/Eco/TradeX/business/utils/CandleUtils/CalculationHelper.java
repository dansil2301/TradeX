package Eco.TradeX.business.utils.CandleUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class CalculationHelper {
    public static BigDecimal calculateAverage(List<BigDecimal> list, RoundingMode roundingMode) {
        BigDecimal sum = list.stream()
                .map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(list.size()), roundingMode);
    }
}
