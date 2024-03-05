package Eco.TradeX.business.Impl.Strategies.MA;

import Eco.TradeX.business.ParameterContainer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class MAParameterContainer implements ParameterContainer {
    private BigDecimal longMA;
    private BigDecimal shortMA;
}
