package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.*;
import Eco.TradeX.business.exceptions.TraderExceptions;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.domain.Requests.EditTraderRequest;
import Eco.TradeX.domain.Response.TradersResponse.CreateTraderResponse;
import Eco.TradeX.domain.Response.TradersResponse.GetTradersResponse;
import Eco.TradeX.domain.Response.TradersResponse.TotalTraders;
import Eco.TradeX.domain.Trader.TraderData;
import Eco.TradeX.domain.Trader.TraderStatus;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final GetTraderPaginatedUseCase getTraderPaginatedUseCase;

    @GetMapping("{id}")
    public ResponseEntity<TraderData> getTrader(@PathVariable(value = "id") final Long id) {
        return ResponseEntity.ok().body(getTradersMethodsUseCase.getTraderById(id));
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping
    public ResponseEntity<GetTradersResponse> getTraders() {
        GetTradersResponse response = GetTradersResponse.builder()
                .traders(getTradersMethodsUseCase.getAllTraders())
                .build();
        return ResponseEntity.ok().body(response);
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/pages")
    ResponseEntity<Page<TraderData>> getTraderPage(@RequestParam(value = "page") Integer page,
                                                   @RequestParam(value = "pageSize") Integer pageSize) {
        try {
            Page<TraderData> traderPage = getTraderPaginatedUseCase.getTraderByPage(page, pageSize);
            return new ResponseEntity<>(traderPage, HttpStatus.OK);
        } catch (TraderExceptions e) {
            return new ResponseEntity<>(getTraderPaginatedUseCase.getTraderByPage(0, pageSize), HttpStatus.OK);
        }
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/search")
    public ResponseEntity<Page<TraderData>> searchTraders(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "searchString") String searchString) {
        try {
            Page<TraderData> traderPage = getTraderPaginatedUseCase.getTraderByUniversalSearch(page, pageSize, searchString);
            return new ResponseEntity<>(traderPage, HttpStatus.OK);
        } catch (TraderExceptions e) {
            return new ResponseEntity<>(getTraderPaginatedUseCase.getTraderByUniversalSearch(0, pageSize, ""), HttpStatus.OK);
        }
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/countAllStatusTraders")
    public ResponseEntity<TotalTraders> countAllTraders(
            @RequestParam(value = "status") TraderStatus status) {
        return new ResponseEntity<>(TotalTraders.builder()
                .total(getTradersMethodsUseCase.getCountOfTradersStatus(status))
                .build(), HttpStatus.OK);
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/countAllTraders")
    public ResponseEntity<TotalTraders> countAllTraders() {
        return new ResponseEntity<>(TotalTraders.builder()
                .total(getTradersMethodsUseCase.getCountAllTraders())
                .build(), HttpStatus.OK);
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
