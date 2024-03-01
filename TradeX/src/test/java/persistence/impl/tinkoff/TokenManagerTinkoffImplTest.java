package persistence.impl.tinkoff;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

class TokenManagerTinkoffImplTest {

    @Test
    void readTokenLocally() {
        var manager = new TokenManagerTinkoffImpl();
        String token = manager.readTokenLocally();
        assertEquals("t.AVH_IIYgRqrERjuodLPcba0eCh82iUCRcGeKpCeVnG-ea9kmUpxRRrj9AtK6_pu41bj11cF90OGScfA2LI7K0Q", token);
    }
}