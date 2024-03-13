package Eco.TradeX.business.Interfaces.TraderServiceInterfaces;

import Eco.TradeX.domain.Requests.CreateTraderRequest;

public interface CreateTraderUseCase {
    Long createTrader(CreateTraderRequest request);
}
