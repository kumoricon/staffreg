package org.kumoricon.staffserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationLogger {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationLogger.class);

    @EventListener
    public void onAuditEvent(AbstractAuthenticationFailureEvent event) {
        LOG.info("Login failed for {}: {}", event.getAuthentication().getPrincipal(), event.getException().getMessage());
    }
}