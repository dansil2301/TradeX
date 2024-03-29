package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyFactoryUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.Response.StrategiesResponse.GetStrategiesNamesResponse;
import Eco.TradeX.domain.Response.StrategiesResponse.GetStrategiesParametersResponse;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/strategies")
@AllArgsConstructor
public class StrategiesController {
    private final StrategyFactoryUseCase strategyFactoryUseCase;
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
        List<CandleData> candles = getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(from, to, figi, interval);
        List<CandleStrategiesParams> parameters = strategyFactoryUseCase.getCandlesStrategiesParameters(strategiesNames, candles, candles.get(0).getTime(), figi, interval);
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
        List<CandleStrategiesParams> parameters = strategyFactoryUseCase.getCandlesStrategiesParameters(strategiesNames, candles, from, figi, interval);
        return ResponseEntity.ok().body(GetStrategiesParametersResponse.builder()
                .to(to)
                .from(from)
                .figi(figi)
                .interval(interval)
                .strategiesParams(parameters)
                .build());
    }

    @GetMapping("/get-strategy-params-fixed-length-candles")
    public ResponseEntity<GetStrategiesParametersResponse> getFixedLengthCandles(@RequestParam(value = "from") Instant from,
                                                                                   @RequestParam(value = "figi") String figi,
                                                                                   @RequestParam(value = "interval") CandleInterval interval,
                                                                                   @RequestParam(value = "candleLength") int candleLength,
                                                                                   @RequestParam(value = "strategiesNames") List<String> strategiesNames,
                                                                                   @RequestParam(value = "isToFuture", required = false) Boolean isToFuture ) {
        List<CandleData> candles;
        if (isToFuture == null || !isToFuture)
        { candles = getCandlesAPIInformationUseCase.getFixedLengthHistoricalCandlesFromAPI(from, figi, interval, candleLength); }
        else
        { candles = getCandlesAPIInformationUseCase.getFixedLengthHistoricalCandlesFromAPIFuture(from, figi, interval, candleLength); }

        List<CandleStrategiesParams> parameters = strategyFactoryUseCase.getCandlesStrategiesParameters(strategiesNames, candles, candles.get(0).getTime(), figi, interval);
        return ResponseEntity.ok().body(GetStrategiesParametersResponse.builder()
                .to(from)
                .from(candles.get(0).getTime())
                .figi(figi)
                .interval(interval)
                .strategiesParams(parameters)
                .build());
    }
}
