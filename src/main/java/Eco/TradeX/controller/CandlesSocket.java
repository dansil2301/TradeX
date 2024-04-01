package Eco.TradeX.controller;

import Eco.TradeX.business.Interfaces.CandleServiceInterfaces.GetCandlesAPIInformationUseCase;
import Eco.TradeX.domain.CandleData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.io.IOException;

import static Eco.TradeX.business.utils.CandleUtils.ConvertToLocalCandleEntity.convertToCandleData;

@Component
@AllArgsConstructor
public class CandlesSocket extends TextWebSocketHandler {
    private final GetCandlesAPIInformationUseCase getCandlesAPIInformationUseCase;
    private static final Logger LOGGER = LoggerFactory.getLogger(CandlesSocket.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        StreamProcessor<MarketDataResponse> processor = response -> {
            if (response.hasCandle()) {
                CandleData candleData = convertToCandleData(response.getCandle());
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    String candleJson = objectMapper.writeValueAsString(candleData);
                    session.sendMessage(new TextMessage(candleJson));
                } catch (IOException e) {
                    LOGGER.error("Error sending candle data to WebSocket session: " + e.getMessage());
                }
            }
        };

        String figi = message.getPayload();
        getCandlesAPIInformationUseCase.candleStreaming("BBG004730N88", processor);
    }
}
