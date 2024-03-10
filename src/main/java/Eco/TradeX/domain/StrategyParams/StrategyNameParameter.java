package Eco.TradeX.domain.StrategyParams;

import Eco.TradeX.business.Interfaces.StrategiesServiceinterface.ParameterContainer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StrategyNameParameter {
    private String strategyName;
    private ParameterContainer parameters;
}
