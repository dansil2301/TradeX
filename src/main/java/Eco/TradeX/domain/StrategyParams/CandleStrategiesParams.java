package Eco.TradeX.domain.StrategyParams;

import Eco.TradeX.business.ParameterContainer;
import Eco.TradeX.domain.CandleData;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class CandleStrategiesParams {
    private CandleData candle;
    private List<StrategyNameParameter> strategyNameParameters;
}
