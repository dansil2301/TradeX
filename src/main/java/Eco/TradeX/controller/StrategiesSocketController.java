package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCurrentCandleIntervalParametersUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyFactoryUseCase;
import Eco.TradeX.business.Interfaces.StrategiesServiceinterfaces.StrategyStreamingHelperUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.domain.Requests.GetSocketMarketStreaming;
import Eco.TradeX.domain.StrategyParams.CandleStrategiesParams;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.util.List;

import static Eco.TradeX.business.utils.CandleUtils.ConvertToLocalCandleEntity.convertToCandleData;

@Controller
@AllArgsConstructor
public class StrategiesSocketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StrategiesSocketController.class);
    SimpMessagingTemplate template;
    private final StrategyStreamingHelperUseCase strategyStreamingHelperUseCase;

    @MessageMapping("/start-live-data")
    public void getLiveMarketData(GetSocketMarketStreaming request) {
        StreamProcessor<MarketDataResponse> processor = response -> {
            if (response.hasCandle()) {
                CandleStrategiesParams candleStrategiesParams = strategyStreamingHelperUseCase.CalculateCandleParams(request, response);

                try {
                    template.convertAndSend("/topic/live-data-message", candleStrategiesParams);
                } catch (Exception e) {
                    LOGGER.error("Error sending candle data to WebSocket session: " + e.getMessage());
                }
            }
        };

        strategyStreamingHelperUseCase.initializeStreamingServices(processor, request);
    }
}
