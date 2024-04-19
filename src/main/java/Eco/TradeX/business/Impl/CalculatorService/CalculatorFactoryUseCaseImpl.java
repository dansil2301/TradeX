package Eco.TradeX.business.Impl.CalculatorService;

import Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces.CalculatorFactoryUseCase;
import Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces.CalculatorStrategyUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CalculatorFactoryUseCaseImpl implements CalculatorFactoryUseCase {
    private final List<CalculatorStrategyUseCase> calculatorStrategyUseCases;

    @Override
    public List<String> getStrategiesNames() {
        List<String> names = new ArrayList<>();

        for (var strategy : calculatorStrategyUseCases) {
            names.add(strategy.getStrategyName());
        }

        return names;
    }

    @Override
    public Double calculateProfitForPeriod(Instant from, Instant to) {
        return null;
    }
}
