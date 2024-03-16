package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.CreateTraderUseCase;
import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.GetTradersMethodsUseCase;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.domain.Response.StrategiesResponse.GetStrategiesNamesResponse;
import Eco.TradeX.domain.Response.TradersResponse.CreateTraderResponse;
import Eco.TradeX.domain.Response.TradersResponse.GetTradersResponse;
import Eco.TradeX.domain.Trader.TraderData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import static Eco.TradeX.business.utils.ServerURLResolver.getServerURL;

@RestController
@RequestMapping("/api/traders")
@AllArgsConstructor
public class TradersController {
    private final GetTradersMethodsUseCase getTradersMethodsUseCase;
    private final CreateTraderUseCase createTraderUseCase;

    @GetMapping()
    public ResponseEntity<TraderData> getTrader(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok().body(getTradersMethodsUseCase.getTraderById(id));
    }

    @GetMapping()
    public ResponseEntity<GetTradersResponse> getTraders() {
        GetTradersResponse response = GetTradersResponse.builder()
                .traders(getTradersMethodsUseCase.getAllTraders())
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/create-trader")
    public ResponseEntity<CreateTraderResponse> createTrader(@RequestBody @Valid CreateTraderRequest request, HttpServletRequest servletRequest) {
        Long id = createTraderUseCase.createTrader(request);
        String URL = getServerURL(servletRequest) + "/api/traders/get-trader-by-id?id=" + id;
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateTraderResponse.builder()
                .requestForNewTrader(URL)
                .build());
    }

    @DeleteMapping("/delete-trader")
    public ResponseEntity<Void> deleteStudent(@RequestParam(value = "id") Long id) {
        return null;
    }

    @PutMapping("/update-trader")
    public ResponseEntity<Void> updateStudent(@RequestParam(value = "id") Long id,
                                              @RequestBody @Valid CreateTraderRequest request,
                                              HttpServletRequest servletRequest) {
        return null;
    }
}
