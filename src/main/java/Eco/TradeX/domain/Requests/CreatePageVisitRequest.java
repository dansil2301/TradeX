package Eco.TradeX.domain.Requests;

import Eco.TradeX.domain.Statistics.Pages;
import lombok.Data;

import java.util.Date;

@Data
public class CreatePageVisitRequest {
    private Long userId;
    private Pages pageName;
}
