package Eco.TradeX.business.Impl.TradersService;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.GetTraderPaginatedUseCase;
import Eco.TradeX.business.exceptions.TraderExceptions;
import Eco.TradeX.business.exceptions.UnauthorizedDataAccessException;
import Eco.TradeX.business.utils.TraderUtils.TraderConverter;
import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Trader.TraderData;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Repositories.TraderRepository.TraderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class GetTradersPaginatedUseCaseImpl implements GetTraderPaginatedUseCase {
    private final TraderRepository traderRepository;
    private final TraderConverter converter;
    private final AccessToken requestAccessToken;

    @Override
    public Page<TraderData> getTraderByPage(int pageNumber, int pageSize) {
        if (requestAccessToken.getStatus() != TraderStatus.ADMIN) {
            throw new UnauthorizedDataAccessException("NOT ADMIN");
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<TraderEntity> traderPage = traderRepository.findAllByOrderByUsernameAsc(pageable);

        if (traderPage.isEmpty()) {
            throw new TraderExceptions("No trader found on page: " + pageNumber);
        }

        return traderPage.map(converter::convertToTraderData);
    }

    @Override
    public Page<TraderData> getTraderByUniversalSearch(int pageNumber, int pageSize, String searchString) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<TraderEntity> traderPage = traderRepository.searchTraderEntityBy(searchString, pageable);

        if (traderPage.isEmpty()) {
            throw new TraderExceptions("No trader found on page: " + pageNumber);
        }

        return traderPage.map(converter::convertToTraderData);
    }
}
