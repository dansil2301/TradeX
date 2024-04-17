package Eco.TradeX.configuration;

import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.configuration.security.token.AccessTokenDecoder;
import Eco.TradeX.controller.StrategiesSocketController;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.*;

import java.util.Collections;


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private AccessTokenDecoder accessTokenDecoder;
    private static final String SPRING_SECURITY_ROLE_PREFIX = "ROLE_";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/live-data").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/live-data").setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                try {
                    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                    if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                        String token = accessor.getFirstNativeHeader("Authorization");
                        if (token != null && token.startsWith("Bearer ")) {
                            String authToken = token.substring(7);
                            AccessToken accessToken = accessTokenDecoder.decode(authToken);

                            UserDetails userDetails = new User(accessToken.getSubject(), "",
                                    Collections.singleton(new SimpleGrantedAuthority(SPRING_SECURITY_ROLE_PREFIX + accessToken.getStatus())));

                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            authenticationToken.setDetails(accessToken);
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                            accessor.setUser(authenticationToken);
                        }
                    }
                } catch (Exception e) {
                    throw new BadCredentialsException("Authentication failed: " + e.getMessage());
                }

                return message;
            }
        });
    }
}