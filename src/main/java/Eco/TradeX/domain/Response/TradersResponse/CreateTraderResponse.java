package Eco.TradeX.domain.Response.TradersResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTraderResponse {
    private String requestForNewTrader;
}
