package Eco.TradeX.business.Impl.TradersService;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.GetTradersMethodsUseCase;
import Eco.TradeX.business.utils.TraderUtils.TraderConverter;
import Eco.TradeX.domain.Trader.TraderData;
import Eco.TradeX.persistence.Impl.TraderRepository.TraderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetTradersMethodsUseCaseImpl implements GetTradersMethodsUseCase {
    private final TraderConverter traderConverter;
    private final TraderRepository traderRepository;

    @Override
    public TraderData getTraderById(Long id) {
        var traderEntity = traderRepository.findById(id);
        return traderConverter.convertToTraderData(traderEntity);
    }

    @Override
    public List<TraderData> getAllTraders() {
        var tradersEntities = traderRepository.findAll();
        return traderConverter.convertToTraderData(tradersEntities);
    }
}
