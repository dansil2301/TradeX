package Eco.TradeX.domain.Response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetStrategiesNamesResponse {
    private List<String> strategyNames;
}
