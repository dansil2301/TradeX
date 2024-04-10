package business.utils;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static Eco.TradeX.business.utils.ServerURLResolver.getServerURL;
import static org.junit.jupiter.api.Assertions.*;

class ServerURLResolverTest {

    @Test
    public void testHttpServerURL() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("example.com");
        request.setServerPort(8080);

        String expectedURL = "http://example.com:8080";
        String actualURL = getServerURL(request);

        assertEquals(expectedURL, actualURL);
    }

    @Test
    public void testHttpsServerURL() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("https");
        request.setServerName("example.com");
        request.setServerPort(8443);

        String expectedURL = "https://example.com:8443";
        String actualURL = getServerURL(request);

        assertEquals(expectedURL, actualURL);
    }

    @Test
    public void testHttpDefaultPortServerURL() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("example.com");
        request.setServerPort(80);

        String expectedURL = "http://example.com";
        String actualURL = getServerURL(request);

        assertEquals(expectedURL, actualURL);
    }

    @Test
    public void testHttpsDefaultPortServerURL() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("https");
        request.setServerName("example.com");
        request.setServerPort(443);

        String expectedURL = "https://example.com";
        String actualURL = getServerURL(request);

        assertEquals(expectedURL, actualURL);
    }
}