package Eco.TradeX.domain.Requests;

import Eco.TradeX.domain.Trader.TraderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditTraderRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotNull
    private TraderStatus status;
}
