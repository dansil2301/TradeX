package Eco.TradeX.domain.StrategyParams;

import Eco.TradeX.domain.CandleData;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CandleStrategiesParams {
    private CandleData candle;
    private List<StrategyNameParameter> strategyNameParameters;
}
