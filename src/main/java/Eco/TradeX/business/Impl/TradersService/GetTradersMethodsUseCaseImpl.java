package Eco.TradeX.business.Impl.TradersService;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.GetTradersMethodsUseCase;
import Eco.TradeX.business.exceptions.UnauthorizedDataAccessException;
import Eco.TradeX.business.utils.TraderUtils.TraderConverter;
import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Trader.TraderData;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Repositories.TraderRepository.TraderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GetTradersMethodsUseCaseImpl implements GetTradersMethodsUseCase {
    private final TraderConverter traderConverter;
    private final TraderRepository traderRepository;
    private AccessToken requestAccessToken;

    @Override
    public TraderData getTraderById(Long id) {
        if (!Objects.equals(requestAccessToken.getId(), id) && requestAccessToken.getStatus() != TraderStatus.ADMIN) {
            throw new UnauthorizedDataAccessException("TRADER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        var traderEntity = traderRepository.findById(id);
        return traderConverter.convertToTraderData(traderEntity);
    }

    @Override
    public List<TraderData> getAllTraders() {
        if (requestAccessToken.getStatus() != TraderStatus.ADMIN) {
            throw new UnauthorizedDataAccessException("TRADER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        var tradersEntities = traderRepository.findAll();
        return traderConverter.convertToTraderData(tradersEntities);
    }

    @Override
    public Long getCountOfTradersStatus(TraderStatus status) {
        return traderRepository.countByStatus(status);
    }

    @Override
    public Long getCountAllTraders() {
        return traderRepository.count();
    }
}
