package com.example.socialApp.security;

import com.example.socialApp.security.jwt.JwtUtils;
import com.example.socialApp.security.services.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.messaging.*;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    private final DefaultSimpUserRegistry userRegistry = new DefaultSimpUserRegistry();
    private final DefaultUserDestinationResolver resolver = new DefaultUserDestinationResolver(userRegistry);

    @Bean
    @Primary
    public SimpUserRegistry userRegistry() {
        return userRegistry;
    }

    @Bean
    @Primary
    public UserDestinationResolver userDestinationResolver() {
        return resolver;
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpTypeMatchers(SimpMessageType.HEARTBEAT,
                SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT,
                SimpMessageType.SUBSCRIBE, SimpMessageType.OTHER,
                SimpMessageType.CONNECT, SimpMessageType.MESSAGE).permitAll()
                .anyMessage().permitAll()
                .simpDestMatchers("/**").permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    /*
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                Optional<String> token;
                //  StompHeaderAccessor accessor = (StompHeaderAccessor) NativeMessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
                // StompHeaderAccessor accessor =
                //   MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                if (accessor != null) {
                    GenericMessage<?> generic = (GenericMessage<?>) accessor.getHeader("simpConnectMessage");
                    if (generic != null) {
                        SimpMessageHeaderAccessor nativeAccessor = SimpMessageHeaderAccessor.wrap(generic);
                        token = Objects.requireNonNull(nativeAccessor.getNativeHeader("Authorization")).stream().findFirst();
                        //if (SimpMessageType.CONNECT_ACK.equals(accessor.getHeader("simpMessageType"))) {
                        if (jwtUtils.validateJwtToken(token.get())) {
                            String username = jwtUtils.getUsernameFromJwtToken(token.get());
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            System.out.println("USer details" + userDetails);
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                            // authenticationToken.setDetails(message.getHeaders());
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            accessor.setUser(authenticationToken);
                            accessor.setHeader("Sender", username);
                        }
                        //}
                    }
                }
                return message;
            }
        });
    }*/
}
