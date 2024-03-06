package Eco.TradeX.controller;

import Eco.TradeX.business.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.StrategyFactoryUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.Response.GetStrategiesNamesResponse;
import Eco.TradeX.domain.Response.GetStrategiesParametersResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.GetStrategiesResponse;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/strategies")
@AllArgsConstructor
public class StrategiesController {
    private StrategyFactoryUseCase strategyFactoryUseCase;
    private final GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;

    @GetMapping("/get-strategies-names")
    public ResponseEntity<GetStrategiesNamesResponse> getStrategyNames() {
        return ResponseEntity.ok().body(GetStrategiesNamesResponse.builder()
                .strategyNames(strategyFactoryUseCase.getStrategiesNames())
                .build());
    }

    @GetMapping("/get-strategy-params-without-candles")
    public ResponseEntity<GetStrategiesParametersResponse> getPeriodStrategyParams(@RequestParam(value = "from") Instant from,
                                                                                   @RequestParam(value = "to") Instant to,
                                                                                   @RequestParam(value = "figi") String figi,
                                                                                   @RequestParam(value = "interval") CandleInterval interval,
                                                                                   @RequestParam(value = "strategiesNames") List<String> strategiesNames) {
        var candles = getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(from, to, figi, interval);
        var parameters = strategyFactoryUseCase.getCandlesStrategiesParameters(strategiesNames, candles, from, figi, interval);
        return ResponseEntity.ok().body(GetStrategiesParametersResponse.builder()
                .to(to)
                .from(from)
                .figi(figi)
                .interval(interval)
                .strategiesParams(parameters)
                .build());
    }

    @GetMapping("/get-strategy-params-with-candles")
    public ResponseEntity<GetStrategiesParametersResponse> getPeriodStrategyParamsWithCandles(@RequestParam(value = "figi") String figi,
                                                                                              @RequestParam(value = "interval") CandleInterval interval,
                                                                                              @RequestBody List<CandleData> candles,
                                                                                              @RequestParam(value = "strategiesNames") List<String> strategiesNames) {
        Instant from = candles.get(0).getTime();
        Instant to = candles.get(candles.size() - 1).getTime();
        var parameters = strategyFactoryUseCase.getCandlesStrategiesParameters(strategiesNames, candles, from, figi, interval);
        return ResponseEntity.ok().body(GetStrategiesParametersResponse.builder()
                .to(to)
                .from(from)
                .figi(figi)
                .interval(interval)
                .strategiesParams(parameters)
                .build());
    }
}
