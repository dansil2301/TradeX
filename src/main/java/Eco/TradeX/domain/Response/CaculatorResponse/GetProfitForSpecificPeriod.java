package Eco.TradeX.domain.Response.CaculatorResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetProfitForSpecificPeriod {
    private Double amount;
}
