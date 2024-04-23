package Eco.TradeX.domain.Response.Calculator;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetCalculatorNamesResponse {
    private List<String> calculatorNames;
}
