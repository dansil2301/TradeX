package Eco.TradeX.business.Impl.TradersService;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.CreateTraderUseCase;
import Eco.TradeX.business.exceptions.PasswordIsNotStrongEnough;
import Eco.TradeX.business.exceptions.TraderAlreadyExistsException;
import Eco.TradeX.business.exceptions.UnauthorizedDataAccessException;
import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Repositories.TraderRepository.TraderRepository;
import Eco.TradeX.persistence.Entities.TraderEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static Eco.TradeX.business.utils.TraderUtils.PasswordChecker.isPasswordLongEnough;
import static Eco.TradeX.business.utils.TraderUtils.PasswordChecker.isPasswordStrong;

@Service
@AllArgsConstructor
public class CreateTraderUseCaseImpl implements CreateTraderUseCase {
    private final TraderRepository traderRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessToken requestAccessToken;

    @Override
    public Long createTrader(CreateTraderRequest request) {
        if (requestAccessToken.getStatus() == TraderStatus.ADMIN) {
            throw new UnauthorizedDataAccessException("Admins can not be created through API");
        }
        if (traderRepository.existsByEmail(request.getEmail())) {
            throw new TraderAlreadyExistsException("Trader with this email already exists");
        }
        if (!isPasswordLongEnough(request.getPassword())) {
            throw new PasswordIsNotStrongEnough("Password is too short");
        }
        if (!isPasswordStrong(request.getPassword())) {
            throw new PasswordIsNotStrongEnough("Password should contain upper case latter, numbers and special symbols");
        }

        saveNewTrader(request);
        TraderEntity trader = traderRepository.findByEmail(request.getEmail());
        return traderRepository.findByEmail(request.getEmail()).getId();
    }

    private void saveNewTrader(CreateTraderRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        TraderEntity newTrader = TraderEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .status(request.getStatus())
                .registeredAt(LocalDateTime.now())
                .build();

        traderRepository.save(newTrader);
    }
}
