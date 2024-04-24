package Eco.TradeX.business.Interfaces.TraderServiceInterfaces;

import Eco.TradeX.domain.Requests.EditTraderRequest;

public interface EditTraderUseCase {
    void editTrader(Long id, EditTraderRequest request);
}
