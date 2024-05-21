package Eco.TradeX.business.Interfaces.StatisticsServiceInterfaces;

import Eco.TradeX.domain.Response.StatisticsResponse.GetStatisticsResponse;
import Eco.TradeX.domain.Statistics.Pages;

import java.util.Date;
import java.util.List;

public interface GetStatisticsUseCase {
    List<Pages> getPagesNames();
    GetStatisticsResponse getStatisticsPageVisited(Date from, Date to);
    GetStatisticsResponse getStatisticsPageVisited(Date from, Date to, Pages pageName);
}
