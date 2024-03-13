package Eco.TradeX.domain.Response.StrategiesResponse;

import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import lombok.Builder;
import lombok.Data;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class GetStrategiesParametersResponse {
    private Instant from;
    private Instant to;
    private String figi;
    private CandleInterval interval;
    private List<CandleStrategiesParams> strategiesParams;
}
