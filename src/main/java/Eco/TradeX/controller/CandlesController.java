package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.Response.CandlesResponse.GetPeriodCandlesResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.piapi.contract.v1.CandleInterval;


import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/candles")
@AllArgsConstructor
public class CandlesController {
    private final GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;

    @RolesAllowed({"TRADER_BASIC", "TRADER_PLUS"})
    @GetMapping
    public ResponseEntity<GetPeriodCandlesResponse> getCandles(@RequestParam(value = "from") Instant from,
                                                               @RequestParam(value = "to") Instant to,
                                                               @RequestParam(value = "figi") String figi,
                                                               @RequestParam(value = "interval") CandleInterval interval) {
        List<CandleData> candles = getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(from, to, figi, interval);
        return  ResponseEntity.ok().body(GetPeriodCandlesResponse.builder()
                .to(to)
                .from(from)
                .figi(figi)
                .interval(interval)
                .candles(candles)
                .build());
    }
}
