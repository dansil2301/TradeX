package Eco.TradeX.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.protobuf.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class CandleData {
    private BigDecimal close;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private long volume;

    @JsonIgnore
    private Timestamp time;
}
