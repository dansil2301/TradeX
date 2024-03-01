package persistence.impl.tinkoff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import persistence.TokenManagerRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Repository
public class TokenManagerTinkoffImpl implements TokenManagerRepository {
    private static final String TOKEN_FILE_PATH = new File("src/main/resources/tinkoffToken.txt").getAbsolutePath();
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManagerTinkoffImpl.class);

    public String readTokenLocally() {
        StringBuilder token = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(TOKEN_FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                token.append(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Error reading tinkoff token: " + e.getLocalizedMessage());
        }
        return token.toString();
    }
}
