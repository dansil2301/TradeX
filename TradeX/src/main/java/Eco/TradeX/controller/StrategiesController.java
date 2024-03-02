package Eco.TradeX.controller;

import Eco.TradeX.business.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.GetStrategyParamsUseCase;
import Eco.TradeX.domain.Response.GetCandlesResponse;
import Eco.TradeX.domain.Response.GetStrategiesResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;

@RestController
@RequestMapping("/strategies")
@AllArgsConstructor
public class StrategiesController {
    private final GetStrategyParamsUseCase getStrategyParamsUseCase;
    private final GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;

    @GetMapping("/getPeriodStrategyParams")
    public ResponseEntity<GetStrategiesResponse> getPeriodStrategyParams(@RequestParam(value = "from") Instant from,
                                                                      @RequestParam(value = "to") Instant to,
                                                                      @RequestParam(value = "figi") String figi,
                                                                      @RequestParam(value = "interval") CandleInterval interval,
                                                                      @RequestParam(value = "strategy") String strategy) {
        var candles = getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(from, to, figi, interval);
        var parameters = getStrategyParamsUseCase.getStrategyParametersForCandles(candles, from, to, figi, interval);
        return  ResponseEntity.ok().body(GetStrategiesResponse.builder()
                    .to(to)
                    .from(from)
                    .figi(figi)
                    .interval(interval)
                    .candles(candles)
                    .strategyParameters(parameters)
                    .build());
    }
}
