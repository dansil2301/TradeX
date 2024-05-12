package Eco.TradeX.business.Interfaces.TraderServiceInterfaces;

import Eco.TradeX.domain.Trader.TraderData;
import org.springframework.data.domain.Page;

public interface GetTraderPaginatedUseCase {
    Page<TraderData> getTraderByPage(int pageNumber, int pageSize);
    Page<TraderData> getTraderByUniversalSearch(int pageNumber, int pageSize, String searchString);
}
