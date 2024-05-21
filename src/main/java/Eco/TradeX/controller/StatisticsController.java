package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.StatisticsServiceInterfaces.GetStatisticsUseCase;
import Eco.TradeX.domain.Response.StatisticsResponse.GetStatisticsResponse;
import Eco.TradeX.domain.Statistics.Pages;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@AllArgsConstructor
public class StatisticsController {
    private final GetStatisticsUseCase getStatisticsUseCase;

    @RolesAllowed({"ADMIN"})
    @GetMapping("/get-pages-names")
    public ResponseEntity<List<Pages>> getStrategyNames() {
        return ResponseEntity.ok().body(getStatisticsUseCase.getPagesNames());
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/get-statistics-pages-visited")
    public ResponseEntity<GetStatisticsResponse> getStatisticsPagesVisited(
            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {

        return ResponseEntity.ok().body(getStatisticsUseCase.getStatisticsPageVisited(from, to));
    }
}
