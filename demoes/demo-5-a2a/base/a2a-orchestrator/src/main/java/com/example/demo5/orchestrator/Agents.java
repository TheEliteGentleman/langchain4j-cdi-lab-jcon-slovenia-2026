package com.example.demo5.orchestrator;

// TODO: Import the necessary LangChain4j Agentic annotations
// import dev.langchain4j.agentic.Agent;
// import dev.langchain4j.agentic.scope.AgenticScopeAccess;
// import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
// import dev.langchain4j.service.UserMessage;
// import dev.langchain4j.service.V;

/**
 * Interfaces of the agents used by the orchestrator.
 * Each interface represents an agent in the Agentic pipeline.
 * 
 * TODO: To complete:
 * 1. Define A2ACreativeWriter with @Agent(description, outputKey="story")
 * 2. Define A2AStyleScorer with @Agent(description, outputKey="score")
 * 3. Define StyleEditor with @UserMessage and @Agent(description, outputKey="story")
 * 4. Define StyleReviewLoop with @Agent
 * 5. Define StyledWriter, which extends AgenticScopeAccess, with @Agent
 */
public class Agents {

    // TODO: Interface for the A2A Creative Writer agent
    // @Agent(description = "Generate a Norse saga based on the given topic", outputKey = "story")
    // String generateStory(@V("topic") String topic);
    public interface A2ACreativeWriter {
        String generateStory(String topic);
    }

    // TODO: Interface for the A2A Style Scorer agent
    // @Agent(description = "Score a saga based on how well it captures a given style", outputKey = "score")
    // double scoreStyle(@V("story") String story, @V("style") String style);
    public interface A2AStyleScorer {
        double scoreStyle(String story, String style);
    }

    // TODO: Interface for the StyleEditor local agent
    // Add @UserMessage with the style edit prompt:
    // """
    // You are a master Norse skald who shapes and tempers sagas like a smith forges steel.
    // Rewrite the following saga to better honor and embody the {{style}} style.
    // Return only the saga and nothing else.
    // The saga is "{{story}}".
    // """
    // Add @Agent(description = "Reforge a saga to better capture a given style", outputKey = "story")
    public interface StyleEditor {
        String editStory(String story, String style);
    }

    // TODO: Interface for the style revision loop
    // @Agent("Judge the saga by the standards of the specified style, as a Viking elder would at the Thing")
    public interface StyleReviewLoop {
        String scoreAndReview(String story, String style);
    }

    // TODO: Main interface for complete orchestration
    // Must extend AgenticScopeAccess to access the agentic scope
    // @Agent
    // ResultWithAgenticScope<String> writeStoryWithStyle(@V("topic") String topic, @V("style") String style);
    public interface StyledWriter {
        String writeStoryWithStyle(String topic, String style);
    }
}
