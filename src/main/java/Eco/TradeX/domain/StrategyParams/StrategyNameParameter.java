package Eco.TradeX.domain.StrategyParams;

import Eco.TradeX.business.ParameterContainer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class StrategyNameParameter {
    private String strategyName;
    private ParameterContainer parameters;
}
