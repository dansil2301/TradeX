package Eco.TradeX.domain.Response.StatisticsResponse;

import Eco.TradeX.domain.Statistics.StatisticsItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetStatisticsResponse {
    private List<StatisticsItem> statisticsPageVisited;
}
