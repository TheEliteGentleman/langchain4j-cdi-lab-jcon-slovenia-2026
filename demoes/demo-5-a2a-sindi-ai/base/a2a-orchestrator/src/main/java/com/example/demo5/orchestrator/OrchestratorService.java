package com.example.demo5.orchestrator;

// TODO: Import the necessary LangChain4j Agentic classes
// import dev.langchain4j.agentic.AgenticServices;
// import dev.langchain4j.agentic.UntypedAgent;
// import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
// TODO: Import agent interfaces
// import com.example.demo5.orchestrator.Agents.A2ACreativeWriter;
// import com.example.demo5.orchestrator.Agents.A2AStyleScorer;
// import com.example.demo5.orchestrator.Agents.StyleEditor;
// import com.example.demo5.orchestrator.Agents.StyledWriter;

/**
 * Orchestration service that coordinates A2A agents.
 * 
 * TODO: To be completed in init():
 * STEP 1: Create the A2A CreativeWriter agent via AgenticServices.a2aBuilder()
 * STEP 2: Create the A2A StyleScorer agent via AgenticServices.a2aBuilder()
 * STEP 3: Create the local StyleEditor agent via AgenticServices.agentBuilder()
 *         using the injected chatModel
 * STEP 4: Create the review loop via AgenticServices.loopBuilder()
 *         with exitCondition: score >= 0.8, maxIterations: 5
 * STEP 5: Create the complete sequence via AgenticServices.sequenceBuilder()
 */
@ApplicationScoped
public class OrchestratorService {

    @Inject
    @ConfigProperty(name = "a2a.creative-writer.url", defaultValue = "http://localhost:8080")
    String creativeWriterUrl;

    @Inject
    @ConfigProperty(name = "a2a.style-scorer.url", defaultValue = "http://localhost:8081")
    String styleScorerUrl;

    @Inject
    @Named("ollama")
    ChatModel chatModel;

    // TODO: Declare the StyledWriter field
    // private StyledWriter styledWriter;

    @PostConstruct
    void init() {
        // TODO STEP 1: Create the A2A CreativeWriter agent
        // A2ACreativeWriter creativeWriter = AgenticServices.a2aBuilder(creativeWriterUrl, A2ACreativeWriter.class)
        //         .outputKey("story")
        //         .build();

        // TODO STEP 2: Create the A2A StyleScorer agent
        // A2AStyleScorer styleScorer = AgenticServices.a2aBuilder(styleScorerUrl, A2AStyleScorer.class)
        //         .outputKey("score")
        //         .build();

        // TODO STEP 3: Create the local StyleEditor agent
        // StyleEditor styleEditor = AgenticServices.agentBuilder(StyleEditor.class)
        //         .chatModel(chatModel)
        //         .outputKey("story")
        //         .build();

        // TODO STEP 4: Create the review loop
        // UntypedAgent styleReviewLoop = AgenticServices.loopBuilder()
        //         .subAgents(styleScorer, styleEditor)
        //         .maxIterations(5)
        //         .exitCondition(scope -> {
        //             Object raw = scope.readState("score");
        //             if (raw == null) return false;
        //             if (raw instanceof Number n) return n.doubleValue() >= 0.8;
        //             try { return Double.parseDouble(raw.toString()) >= 0.8; }
        //             catch (NumberFormatException e) { return false; }
        //         })
        //         .build();

        // TODO STEP 5: Create the complete sequence
        // styledWriter = AgenticServices.sequenceBuilder(StyledWriter.class)
        //         .subAgents(creativeWriter, styleReviewLoop)
        //         .outputKey("story")
        //         .build();
    }

    public String writeStyledStory(String topic, String style) {
        // TODO: Call styledWriter.writeStoryWithStyle() and return the result
        // ResultWithAgenticScope<String> result = styledWriter.writeStoryWithStyle(topic, style);
        // return result.result();
        throw new UnsupportedOperationException("TODO: Implement the orchestration");
    }
}
