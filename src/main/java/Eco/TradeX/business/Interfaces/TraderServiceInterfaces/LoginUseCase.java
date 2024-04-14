package Eco.TradeX.business.Interfaces.TraderServiceInterfaces;

import Eco.TradeX.domain.Trader.LoginRequest;
import Eco.TradeX.domain.Trader.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}
