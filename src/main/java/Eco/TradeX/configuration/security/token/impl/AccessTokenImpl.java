package Eco.TradeX.configuration.security.token.impl;

import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Trader.TraderStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Long id;
    private final TraderStatus status;

    public AccessTokenImpl(String subject, Long id, TraderStatus status) {
        this.subject = subject;
        this.id = id;
        this.status = status;
    }

    @Override
    public boolean hasRole(String roleName) {
        return this.status == TraderStatus.valueOf(roleName);
    }
}
