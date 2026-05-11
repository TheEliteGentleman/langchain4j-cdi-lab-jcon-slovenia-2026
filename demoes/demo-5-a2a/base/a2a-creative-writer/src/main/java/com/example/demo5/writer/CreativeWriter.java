package com.example.demo5.writer;

// TODO: Import the necessary LangChain4j annotations
// import dev.langchain4j.cdi.spi.RegisterAIService;
// import dev.langchain4j.service.UserMessage;
// import dev.langchain4j.service.V;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * AI agent "Creative Writer" that generates short stories.
 * 
 * TODO: To complete:
 * 1. Annotate the interface with @RegisterAIService(chatModelName = "ollama", scope = ApplicationScoped.class)
 * 2. Add @UserMessage with the story generation prompt
 * 3. Use @V("topic") to inject the topic into the template
 */
public interface CreativeWriter {

    // TODO: Add the @UserMessage annotation with the following prompt:
    // """
    // You are a Norse skald, a Viking bard who crafts mighty sagas of glory, battle, and adventure.
    // Forge a short saga no more than 3 sentences around the given topic, in the spirit of the Viking age.
    // Return only the saga and nothing else.
    // The topic is {{topic}}.
    // """
    String generateStory(/* TODO: @V("topic") */ String topic);
}
