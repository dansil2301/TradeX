package Eco.TradeX.business.Interfaces.StatisticsServiceInterfaces;

import Eco.TradeX.domain.Requests.CreatePageVisitRequest;
import Eco.TradeX.domain.Requests.CreateTraderRequest;

public interface CreateStatisticsUseCase {
    Long createPageVisit(CreatePageVisitRequest request);
}
