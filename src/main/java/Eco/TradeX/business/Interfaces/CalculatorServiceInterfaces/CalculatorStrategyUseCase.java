package Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces;

import Eco.TradeX.domain.Calculator.ActionSignal;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;

public interface CalculatorStrategyUseCase {
    String getStrategyName();
    ActionSignal ActionForThisCandle(CandleStrategiesParams candleParam);
}
