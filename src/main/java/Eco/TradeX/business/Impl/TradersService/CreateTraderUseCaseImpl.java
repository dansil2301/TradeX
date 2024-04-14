package Eco.TradeX.business.Impl.TradersService;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.CreateTraderUseCase;
import Eco.TradeX.business.exceptions.TraderAlreadyExistsException;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.persistence.Impl.TraderRepository.TraderRepository;
import Eco.TradeX.persistence.Entities.TraderEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateTraderUseCaseImpl implements CreateTraderUseCase {
    private final TraderRepository traderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long createTrader(CreateTraderRequest request) {
        if (traderRepository.existsByEmail(request.getEmail())) {
            throw new TraderAlreadyExistsException("Trader with this email already exists");
        }

        saveNewTrader(request);
        return traderRepository.findByEmail(request.getEmail()).getId();
    }

    private void saveNewTrader(CreateTraderRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        TraderEntity newTrader = TraderEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .status(request.getStatus())
                .build();

        traderRepository.save(newTrader);
    }
}
