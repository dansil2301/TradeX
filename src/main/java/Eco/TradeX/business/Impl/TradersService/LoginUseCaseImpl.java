package Eco.TradeX.business.Impl.TradersService;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.LoginUseCase;
import Eco.TradeX.business.exceptions.InvalidCredentialsException;
import Eco.TradeX.configuration.security.token.AccessTokenEncoder;
import Eco.TradeX.configuration.security.token.impl.AccessTokenImpl;
import Eco.TradeX.domain.Trader.LoginRequest;
import Eco.TradeX.domain.Trader.LoginResponse;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Impl.TraderRepository.TraderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final TraderRepository traderRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        TraderEntity trader = traderRepository.findByEmail(loginRequest.getEmail());
        if (trader == null) {
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(loginRequest.getPassword(), trader.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(trader);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(TraderEntity trader) {
        return accessTokenEncoder.encode(
                new AccessTokenImpl(trader.getEmail(), trader.getId(), trader.getStatus()));
    }
}
