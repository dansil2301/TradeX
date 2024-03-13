package Eco.TradeX.domain.Response.TradersResponse;

import Eco.TradeX.domain.Trader.TraderData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetTradersResponse {
    private List<TraderData> traders;
}
