package Eco.TradeX.business.Interfaces.TraderServiceInterfaces;

import Eco.TradeX.domain.Trader.TraderData;

import java.util.List;

public interface GetTradersMethodsUseCase {
    TraderData getTraderById(Long id);
    List<TraderData> getAllTraders();
}
