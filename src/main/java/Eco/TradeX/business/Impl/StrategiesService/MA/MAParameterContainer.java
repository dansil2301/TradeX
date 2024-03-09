package Eco.TradeX.business.Impl.StrategiesService.MA;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.StrategiesServiceinterface.ParameterContainer;
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
