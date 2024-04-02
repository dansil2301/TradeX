package Eco.TradeX.domain.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetSocketMarketStreaming {
    private String figi;
    private CandleInterval interval;
    private List<String> strategiesNames;
}
