package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.CalculatorServiceInterfaces.CalculatorFactoryUseCase;
import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.Response.CandlesResponse.GetPeriodCandlesResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/calculator")
@AllArgsConstructor
public class CalculatorController {
    private final CalculatorFactoryUseCase calculatorFactoryUseCase;

    @RolesAllowed({"TRADER_PLUS"})
    @GetMapping
    public ResponseEntity<GetPeriodCandlesResponse> getCandles(@RequestParam(value = "from") Instant from,
                                                               @RequestParam(value = "to") Instant to,
                                                               @RequestParam(value = "figi") String figi,
                                                               @RequestParam(value = "interval") CandleInterval interval,
                                                               @RequestParam(value = "strategyName") String strategyName,
                                                               @RequestParam(value = "deposit") Double deposit) {
        return null;
    }
}
