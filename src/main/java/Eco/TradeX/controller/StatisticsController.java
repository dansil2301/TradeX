package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.StatisticsServiceInterfaces.CreateStatisticsUseCase;
import Eco.TradeX.business.Interfaces.StatisticsServiceInterfaces.GetStatisticsUseCase;
import Eco.TradeX.domain.Requests.CreatePageVisitRequest;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.domain.Response.StatisticsResponse.GetStatisticsResponse;
import Eco.TradeX.domain.Statistics.Pages;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@AllArgsConstructor
public class StatisticsController {
    private final GetStatisticsUseCase getStatisticsUseCase;
    private final CreateStatisticsUseCase createStatisticsUseCase;

    @RolesAllowed({"ADMIN"})
    @GetMapping("/get-pages-names")
    public ResponseEntity<List<Pages>> getStrategyNames() {
        return ResponseEntity.ok().body(getStatisticsUseCase.getPagesNames());
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/get-statistics-pages-visited")
    public ResponseEntity<GetStatisticsResponse> getStatisticsPagesVisited(
            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(required = false, value = "pageName") Pages pageName) {

        if (pageName == null)
        { return ResponseEntity.ok().body(getStatisticsUseCase.getStatisticsPageVisited(from, to)); }
        else
        { return ResponseEntity.ok().body(getStatisticsUseCase.getStatisticsPageVisited(from, to, pageName)); }
    }

    @PostMapping()
    public ResponseEntity<Void> createPageVisited(@RequestBody @Valid CreatePageVisitRequest request) {
        createStatisticsUseCase.createPageVisit(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
