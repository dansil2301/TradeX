package Eco.TradeX.business.Impl.TradersService;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.DeleteTraderUseCase;
import Eco.TradeX.business.exceptions.UnauthorizedDataAccessException;
import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Repositories.TraderRepository.TraderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class DeleteTraderUseCaseImpl implements DeleteTraderUseCase {
    private final TraderRepository traderRepository;
    private AccessToken requestAccessToken;

    @Override
    public void deleteTrader(Long id) {
        if (!Objects.equals(requestAccessToken.getId(), id) && requestAccessToken.getStatus() != TraderStatus.ADMIN) {
            throw new UnauthorizedDataAccessException("TRADER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        traderRepository.deleteById(id);
    }
}
