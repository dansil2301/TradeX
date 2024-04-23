package Eco.TradeX.business.Impl.CalculatorService;

import Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces.CalculatorFactoryUseCase;
import Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces.CalculatorStrategyUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.ParameterContainer;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyFactoryUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyUseCase;
import Eco.TradeX.business.exceptions.CalculationException;
import Eco.TradeX.business.exceptions.StrategyExceptions;
import Eco.TradeX.domain.Calculator.ActionSignal;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CalculatorFactoryUseCaseImpl implements CalculatorFactoryUseCase {
    private final List<CalculatorStrategyUseCase> calculatorStrategyUseCases;
    private final StrategyFactoryUseCase strategyFactoryUseCase;
    private final ClientAPIRepository clientAPIRepository;

    @Override
    public List<String> getCalculatorNames() {
        List<String> names = new ArrayList<>();

        for (var strategy : calculatorStrategyUseCases) {
            names.add(strategy.getStrategyName());
        }

        return names;
    }

    @Override
    public Double calculateProfitForPeriod(Instant from, Instant to, String figi, CandleInterval interval, String strategyName, Double deposit) {
        List<String> strategyNames = new ArrayList<>();
        strategyNames.add(strategyName);

        CalculatorStrategyUseCase calculator = getCalculatorInstance(strategyName);
        List<CandleData> candles = clientAPIRepository.getHistoricalCandles(from, to, figi, interval);

        strategyFactoryUseCase.initializeContainers(strategyNames, figi, interval, from);

        boolean isBought = false;
        int stocksBought = 0;
        Double finalAmount = deposit;
        for (CandleData candle : candles) {
            CandleStrategiesParams parameter = strategyFactoryUseCase.calculateParametersForCandle(candle, strategyNames, interval);
            ActionSignal action = calculator.ActionForThisCandle(parameter);

            if (action == ActionSignal.BUY) {
                isBought = true;
                stocksBought = stocksCanBeBought(finalAmount, candle.getClose().doubleValue());
                finalAmount -= candle.getClose().doubleValue() * stocksBought;
            }
            else if (action == ActionSignal.SELL) {
                isBought = false;
                finalAmount += candle.getClose().doubleValue() * stocksBought;
                stocksBought = 0;
            }
        }

        if (isBought && !candles.isEmpty())
        { finalAmount += candles.get(0).getClose().doubleValue() * stocksBought; }

        return roundToTwoDecimalPlaces(finalAmount);
    }

    private Double roundToTwoDecimalPlaces(Double amount) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(amount));
    }

    private int stocksCanBeBought(Double finalAmount, Double candlePrice) {
        return (int) (finalAmount / candlePrice);
    }

    private CalculatorStrategyUseCase getCalculatorInstance(String strategyName) {
        for (CalculatorStrategyUseCase calculatorStrategyUseCase : calculatorStrategyUseCases) {
            if (Objects.equals(strategyName, calculatorStrategyUseCase.getStrategyName())) {
                return calculatorStrategyUseCase;
            }
        }
        throw new CalculationException("Some strategy names from the list don't exist");
    }
}
