package Eco.TradeX.business.Impl.Strategies.RSI;

import Eco.TradeX.business.ParameterContainer;
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
