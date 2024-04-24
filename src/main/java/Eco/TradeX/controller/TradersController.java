package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.CreateTraderUseCase;
import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.DeleteTraderUseCase;
import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.EditTraderUseCase;
import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.GetTradersMethodsUseCase;
import Eco.TradeX.business.exceptions.TraderExceptions;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.domain.Requests.EditTraderRequest;
import Eco.TradeX.domain.Response.TradersResponse.CreateTraderResponse;
import Eco.TradeX.domain.Response.TradersResponse.GetTradersResponse;
import Eco.TradeX.domain.Trader.TraderData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static Eco.TradeX.business.utils.ServerURLResolver.getServerURL;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/traders")
@AllArgsConstructor
public class TradersController {
    private final GetTradersMethodsUseCase getTradersMethodsUseCase;
    private final CreateTraderUseCase createTraderUseCase;
    private final EditTraderUseCase editTraderUseCase;
    private final DeleteTraderUseCase deleteTraderUseCase;

    @GetMapping("{id}")
    public ResponseEntity<TraderData> getTrader(@PathVariable(value = "id") final Long id) {
        return ResponseEntity.ok().body(getTradersMethodsUseCase.getTraderById(id));
    }

    @GetMapping
    public ResponseEntity<GetTradersResponse> getTraders() {
        GetTradersResponse response = GetTradersResponse.builder()
                .traders(getTradersMethodsUseCase.getAllTraders())
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping()
    public ResponseEntity<CreateTraderResponse> createTrader(@RequestBody @Valid CreateTraderRequest request, HttpServletRequest servletRequest) {
        Long id = createTraderUseCase.createTrader(request);
        String URL = getServerURL(servletRequest) + "/api/traders/?id=" + id;
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateTraderResponse.builder()
                .requestForNewTrader(URL)
                .build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable(value = "id") final Long id) {
        try {
            deleteTraderUseCase.deleteTrader(id);
            return ResponseEntity.noContent().build();
        } catch (TraderExceptions e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<EditTraderRequest> updateStudent(@PathVariable(value = "id") final Long id,
                                                           @RequestBody @Valid EditTraderRequest request) {
        try {
            editTraderUseCase.editTrader(id, request);
            return ResponseEntity.noContent().build();
        } catch (TraderExceptions e) {
            return ResponseEntity.notFound().build();
        }
    }
}
