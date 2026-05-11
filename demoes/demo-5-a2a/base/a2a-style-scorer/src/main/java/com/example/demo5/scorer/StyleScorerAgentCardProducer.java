package com.example.demo5.scorer;

import io.a2a.server.PublicAgentCard;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentInterface;
import io.a2a.spec.AgentSkill;
import io.a2a.spec.TransportProtocol;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * CDI Producer of the A2A AgentCard for the Style Scorer.
 * The AgentCard describes the agent's capabilities to other agents on the A2A network.
 * 
 * TODO: To be completed:
 * 1. Implement the agentCard() method to construct and return an AgentCard
 * 2. Configure the capabilities (streaming, etc.)
 * 3. Declare the "style_scorer" skill
 */
@ApplicationScoped
public class StyleScorerAgentCardProducer {

    @Inject
    @ConfigProperty(name = "a2a.server.url", defaultValue = "http://localhost:8080")
    String serverUrl;

    @Produces
    @PublicAgentCard
    public AgentCard agentCard() {
    	// TODO: Build the AgentCard with the Builder: 
    	// - name: "Style Scorer" 
    	// - description: "Score a story based on how well it aligns with a given style" 
    	// - url: serverUrl 
    	// - version: "1.0.0" 
    	// - capabilities: streaming=true, pushNotifications=false, stateTransitionHistory=false 
    	// - defaultInputModes / defaultOutputModes: "text" 
    	// - skills: an AgentSkill with id="style_scorer", name="Style Scorer" 
    	// - protocolVersion: "0.3.0" 
    	// - preferredTransport: TransportProtocol.JSONRPC 
    	// - additionalInterfaces: AgentInterface JSONRPC on serverUrl
        throw new UnsupportedOperationException("TODO: Implement the AgentCard builder");
    }
}
