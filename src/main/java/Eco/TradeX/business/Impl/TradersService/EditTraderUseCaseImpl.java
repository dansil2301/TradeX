package Eco.TradeX.business.Impl.TradersService;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.EditTraderUseCase;
import Eco.TradeX.business.exceptions.TraderExceptions;
import Eco.TradeX.business.exceptions.UnauthorizedDataAccessException;
import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Requests.EditTraderRequest;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Repositories.TraderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class EditTraderUseCaseImpl implements EditTraderUseCase {
    private final TraderRepository traderRepository;
    private AccessToken requestAccessToken;

    @Override
    public void editTrader(Long id, EditTraderRequest request) {
        TraderEntity trader = traderRepository.findById(id)
                .orElseThrow(() -> new TraderExceptions("trader not found with id: " + id));
        if (!Objects.equals(requestAccessToken.getId(), id) && requestAccessToken.getStatus() != TraderStatus.ADMIN) {
            throw new UnauthorizedDataAccessException("TRADER_ID_NOT_FROM_LOGGED_IN_USER");
        }
        if (requestAccessToken.getStatus() != TraderStatus.ADMIN && request.getStatus() == TraderStatus.ADMIN) {
            throw new UnauthorizedDataAccessException("NOBODY CAN CREAT ADMINS FROM ENDPOINTS");
        }

        trader.setUsername(request.getUsername());
        trader.setEmail(request.getEmail());
        trader.setStatus(request.getStatus());

        traderRepository.save(trader);
    }
}
