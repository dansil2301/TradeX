package Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces;

import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

public interface CalculatorFactoryUseCase {
    List<String> getStrategiesNames();
    Double calculateProfitForPeriod(Instant from, Instant to, String figi, CandleInterval interval, String strategyName, Double deposit);
}
