package com.example.demo5.scorer;

// TODO: Import the necessary LangChain4j annotations
// import dev.langchain4j.cdi.spi.RegisterAIService;
// import dev.langchain4j.service.UserMessage;
// import dev.langchain4j.service.V;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * AI agent "Style Scorer" that evaluates the style of a story.
 * 
 * TODO: To complete:
 * 1. Annotate the interface with @RegisterAIService(chatModelName = "ollama", scope = ApplicationScoped.class)
 * 2. Add @UserMessage with the style evaluation prompt
 * 3. Use @V("story") and @V("style") to inject the parameters into the template
 */
public interface StyleScorer {

    // TODO: Add the @UserMessage annotation with the following prompt:
    // """
    // You are a seasoned Norse skald elder who judges sagas by the fire of a longhouse.
    // Give a score between 0.0 and 1.0 for the following saga based on how well it captures the '{{style}}' style.
    // Return only the score and nothing else.
    //
    // The saga is: "{{story}}"
    // """
    double scoreStyle(/* TODO: @V("story") */ String story, /* TODO: @V("style") */ String style);
}
