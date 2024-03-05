package Eco.TradeX.domain.Response;

import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import lombok.Builder;
import lombok.Data;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class GetStrategiesResponse {
    private Instant from;
    private Instant to;
    private String figi;
    private CandleInterval interval;
    private List<CandleStrategiesParams> strategiesParams;
}
