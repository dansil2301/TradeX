package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.domain.CandleData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.io.IOException;

import static Eco.TradeX.business.utils.CandleUtils.ConvertToLocalCandleEntity.convertToCandleData;

@Controller
@AllArgsConstructor
public class CandlesSocketController {
    SimpMessagingTemplate template;
    private final GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;
    private static final Logger LOGGER = LoggerFactory.getLogger(CandlesSocket.class);

    @MessageMapping("/start-live-data")
    public void getLiveMarketData(String figi) {
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

        getCandlesAPIInformationUseCase.candleStreaming(figi, processor);
    }
}
