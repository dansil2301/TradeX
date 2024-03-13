package Eco.TradeX.business.Impl.TradersService;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.CreateTraderUseCase;
import Eco.TradeX.business.exceptions.TraderAlreadyExistsException;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.persistence.Interfaces.TraderRepositoryInterfaces.TraderRepository;
import Eco.TradeX.persistence.Impl.TraderRepository.TraderEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateTraderUseCaseImpl implements CreateTraderUseCase {
    private final TraderRepository traderRepository;

    @Override
    public Long createTrader(CreateTraderRequest request) {
        if (traderRepository.existsByUsername(request.getUsername())) {
            throw new TraderAlreadyExistsException("Trader with this username already exists");
        }
        if (traderRepository.existsByEmail(request.getEmail())) {
            throw new TraderAlreadyExistsException("Trader with this email already exists");
        }

        saveNewTrader(request);
        return traderRepository.findByEmail(request.getEmail()).getId();
    }

    private void saveNewTrader(CreateTraderRequest request) {
        TraderEntity newTrader = TraderEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .status(request.getStatus())
                .build();
        traderRepository.save(newTrader);
    }
}
