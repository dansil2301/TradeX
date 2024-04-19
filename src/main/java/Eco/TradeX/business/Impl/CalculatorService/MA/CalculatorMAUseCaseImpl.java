package Eco.TradeX.business.Impl.CalculatorService.MA;

import Eco.TradeX.business.Impl.StrategiesService.MA.MAParameterContainer;
import Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces.CalculatorStrategyUseCase;
import Eco.TradeX.domain.Calculator.ActionSignal;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import Eco.TradeX.domain.StrategyParams.StrategyNameParameter;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CalculatorMAUseCaseImpl implements CalculatorStrategyUseCase {
    private boolean firstEntry;
    private MAParameterContainer prevParameters;

    public CalculatorMAUseCaseImpl() {
        firstEntry = true;
    }

    @Override
    public String getStrategyName() {
        return "MA";
    }

    @Override
    public ActionSignal ActionForThisCandle(CandleStrategiesParams candleParam) {
        MAParameterContainer curParameters = null;
        for (StrategyNameParameter element : candleParam.getStrategyNameParameters()) {
            if (Objects.equals(element.getStrategyName(), getStrategyName())) {
                curParameters = (MAParameterContainer) element.getParameters();
                break;
            }
        }

        if (firstEntry || prevParameters == null) {
            prevParameters = curParameters;
            return ActionSignal.KEEP;
        }


        firstEntry = false;
        ActionSignal action = getActionSignal(curParameters);
        prevParameters = curParameters;
        return action;
    }

    private ActionSignal getActionSignal(MAParameterContainer curParameters) {
        ActionSignal action;
        // if short is crossing from below -> buy | if from above then sell | anything else keep
        if (prevParameters.getShortMA().compareTo(prevParameters.getLongMA()) < 0 &&
                curParameters.getShortMA().compareTo(curParameters.getLongMA()) > 0) {
            action = ActionSignal.BUY;
        } else if (prevParameters.getShortMA().compareTo(prevParameters.getLongMA()) > 0 &&
                curParameters.getShortMA().compareTo(curParameters.getLongMA()) < 0) {
            action = ActionSignal.SELL;
        }
        else {
            action = ActionSignal.KEEP;
        }
        return action;
    }
}
