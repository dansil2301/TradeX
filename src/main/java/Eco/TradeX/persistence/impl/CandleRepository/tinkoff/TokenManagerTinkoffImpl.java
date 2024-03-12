package Eco.TradeX.persistence.impl.CandleRepository.tinkoff;

import Eco.TradeX.business.exceptions.TokenExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.TokenManagerRepository;

import java.io.*;
import java.util.Properties;

@Component
public class TokenManagerTinkoffImpl {
    @Value("${tinkoffToken}")
    private String token;

    public String readTokenLocally() {
        return token;
    }
}
