package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.domain.CandleData;
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
    SimpMessagingTemplate template;
    private final GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;
    private static final Logger LOGGER = LoggerFactory.getLogger(StrategiesSocketController.class);

    @MessageMapping("/start-live-data")
    public void getLiveMarketData(String figi, CandleInterval interval, List<String> strategiesNames) {
        StreamProcessor<MarketDataResponse> processor = response -> {
            if (response.hasCandle()) {
                CandleData candleData = convertToCandleData(response.getCandle());
                try {
                    template.convertAndSend("/topic/live-data-message", candleData);
                } catch (Exception e) {
                    LOGGER.error("Error sending candle data to WebSocket session: " + e.getMessage());
                }
            }
        };

        getCandlesAPIInformationUseCase.candleStreaming(figi, interval, processor);
    }
}
