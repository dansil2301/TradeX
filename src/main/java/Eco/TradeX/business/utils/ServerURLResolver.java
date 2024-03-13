package Eco.TradeX.business.utils;

import jakarta.servlet.http.HttpServletRequest;

public class ServerURLResolver {
    public static String getServerURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        StringBuilder serverURL = new StringBuilder();
        serverURL.append(scheme).append("://").append(serverName);

        if (("http".equals(scheme) && serverPort != 80) || ("https".equals(scheme) && serverPort != 443)) {
            serverURL.append(":").append(serverPort);
        }

        return serverURL.toString();
    }
}
