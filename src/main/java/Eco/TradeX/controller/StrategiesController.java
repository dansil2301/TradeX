package Eco.TradeX.controller;

import Eco.TradeX.business.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.GetStrategyParamsUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.Response.GetCandlesResponse;
import Eco.TradeX.domain.Response.GetStrategiesResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/strategies")
@AllArgsConstructor
public class StrategiesController {
    private final GetStrategyParamsUseCase getStrategyParamsUseCase;
    private final GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;

    @GetMapping("/strategy-params-without-candles")
    public ResponseEntity<GetStrategiesResponse> getPeriodStrategyParams(@RequestParam(value = "from") Instant from,
                                                                         @RequestParam(value = "to") Instant to,
                                                                         @RequestParam(value = "figi") String figi,
                                                                         @RequestParam(value = "interval") CandleInterval interval,
                                                                         @RequestParam(value = "strategy") String strategy) {
        var candles = getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(from, to, figi, interval);
        var parameters = getStrategyParamsUseCase.getStrategyParametersForCandles(candles, from, to, figi, interval);
        return ResponseEntity.ok().body(GetStrategiesResponse.builder()
                .to(to)
                .from(from)
                .figi(figi)
                .interval(interval)
                .candles(candles)
                .strategyParameters(parameters)
                .build());
    }

    @GetMapping("/strategy-params-with-candles")
    public ResponseEntity<GetStrategiesResponse> getPeriodStrategyParamsWithCandles(@RequestParam(value = "from") Instant from,
                                                                                    @RequestParam(value = "to") Instant to,
                                                                                    @RequestParam(value = "figi") String figi,
                                                                                    @RequestParam(value = "interval") CandleInterval interval,
                                                                                    @RequestBody List<CandleData> candles,
                                                                                    @RequestParam(value = "strategy") String strategy) {
        var parameters = getStrategyParamsUseCase.getStrategyParametersForCandles(candles, from, to, figi, interval);
        return ResponseEntity.ok().body(GetStrategiesResponse.builder()
                .to(to)
                .from(from)
                .figi(figi)
                .interval(interval)
                .candles(candles)
                .strategyParameters(parameters)
                .build());
    }
}
