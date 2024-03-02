package Eco.TradeX.domain.Requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class GetCandlesRequest {
    @NonNull
    private Instant from;
    @NonNull
    private  Instant to;
    @NotBlank
    private  String figi;
    @NonNull
    private CandleInterval interval;
}
