package Eco.TradeX.configuration.security.token;

import Eco.TradeX.domain.Trader.TraderStatus;

import java.util.Set;

public interface AccessToken {
    String getSubject();

    Long getId();

    TraderStatus getStatus();

    boolean hasRole(String roleName);
}
