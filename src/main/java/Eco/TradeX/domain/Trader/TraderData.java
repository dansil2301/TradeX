package Eco.TradeX.domain.Trader;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class TraderData {
    private Long id;
    private String username;
    private String email;
    private String password;
    private TraderStatus status;
    private LocalDateTime createdAt;
}
