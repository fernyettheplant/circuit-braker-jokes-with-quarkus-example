package dev.fernyettheplant.ui.kafka.joke;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class JokeIngestedConsumer {
    private static final Logger log = Logger.getLogger(JokeIngestedConsumer.class);
    
    @Incoming("joke-ingested-in")
    public void consume(byte[] message) {
        log.info("Message received");
    }
}