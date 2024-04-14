package Eco.TradeX.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
