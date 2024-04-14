package Eco.TradeX.configuration.security.token.impl;

import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.configuration.security.token.AccessTokenDecoder;
import Eco.TradeX.configuration.security.token.AccessTokenEncoder;
import Eco.TradeX.configuration.security.token.exception.InvalidAccessTokenException;
import Eco.TradeX.domain.Trader.TraderStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import io.jsonwebtoken.io.Decoders;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccessTokenEncoderDecoderImpl implements AccessTokenDecoder, AccessTokenEncoder {
    private final Key key;

    public AccessTokenEncoderDecoderImpl(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String encode(AccessToken accessToken) {
        Map<String, Object> claimsMap = new HashMap<>();
        if (accessToken.getStatus() != null) {
            claimsMap.put("status", accessToken.getStatus());
        }
        if (accessToken.getId() != null) {
            claimsMap.put("id", accessToken.getId());
        }

        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(accessToken.getSubject())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(30, ChronoUnit.MINUTES)))
                .addClaims(claimsMap)
                .signWith(key)
                .compact();
    }

    @Override
    public AccessToken decode(String accessTokenEncoded) {
        try {
            Jwt<?, Claims> jwt = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(accessTokenEncoded);
            Claims claims = jwt.getBody();

            String statusString = claims.get("status", String.class);
            TraderStatus status = TraderStatus.valueOf(statusString);

            Long id = claims.get("id", Long.class);

            return new AccessTokenImpl(claims.getSubject(), id, status);
        } catch (JwtException e) {
            throw new InvalidAccessTokenException(e.getMessage());
        }
    }
}
