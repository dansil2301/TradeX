package Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces;

import java.time.Instant;
import java.util.List;

public interface CalculatorFactoryUseCase {
    List<String> getStrategiesNames();
    Double calculateProfitForPeriod(Instant from, Instant to);
}
