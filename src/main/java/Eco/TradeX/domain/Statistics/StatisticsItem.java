package Eco.TradeX.domain.Statistics;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StatisticsItem {
    private Long tradersVisited;
    private Date visitedAt;
}
