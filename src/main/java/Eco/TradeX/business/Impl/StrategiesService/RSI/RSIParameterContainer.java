package Eco.TradeX.business.Impl.StrategiesService.RSI;

import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.ParameterContainer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class RSIParameterContainer implements ParameterContainer {
    private BigDecimal RSI;
}
