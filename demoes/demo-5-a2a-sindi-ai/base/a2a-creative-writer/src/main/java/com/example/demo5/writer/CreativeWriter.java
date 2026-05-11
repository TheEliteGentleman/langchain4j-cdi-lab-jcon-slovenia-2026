package com.example.demo5.writer;

// TODO: Import the necessary LangChain4j annotations
// import dev.langchain4j.cdi.spi.RegisterAIService;
// import dev.langchain4j.service.UserMessage;
// import dev.langchain4j.service.V;
import jakarta.enterprise.context.ApplicationScoped;
import za.co.sindi.ai.a2a.server.spi.Agent;
import za.co.sindi.ai.a2a.server.spi.PublicCard;

/**
 * AI agent "Creative Writer" that generates short stories.
 * 
 * TODO: To complete:
 * 1. Annotate the interface with @RegisterAIService(chatModelName = "ollama", scope = ApplicationScoped.class)
 * 2. Annotate the interface with @Agent(name="Creative Writer", description = "Generate a story based on the given topic")
 * 3. Annotate the interface with @PublicCard(version = "1.0.0", defaultInputModes = { "text" }, defaultOutputModes = { "text" })
 * 4. Add @UserMessage with the story generation prompt
 * 5. Use @V("topic") to inject the topic into the template
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
