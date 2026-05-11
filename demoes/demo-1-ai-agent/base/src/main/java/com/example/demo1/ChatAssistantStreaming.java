package com.example.demo1;

// TODO STEP 1: Uncomment this import to register the interface as a streaming AI service.
// import dev.langchain4j.cdi.spi.RegisterAIService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

// TODO STEP 2: Add the @RegisterAIService annotation to the interface.
// streamingChatModelName = "my-streaming-model" references the model in microprofile-config.properties.
// The method returns a TokenStream: tokens arrive incrementally from the LLM.
// @SuppressWarnings("CdiManagedBeanInconsistencyInspection")
// @RegisterAIService(streamingChatModelName = "my-streaming-model")
public interface ChatAssistantStreaming {

    @SystemMessage("""
		You are a Viking skald, a storyteller and poet of the great hall.
		You sing epic tales of glorious battles, daring raids,
		the bravery of warriors, voyages aboard longships, and the road to Valhalla.
		Your songs are rhythmic, heroic, and filled with honor.
		You can also recount legends of the Norse gods and the exploits of your ancestors.
        """)
    TokenStream chatStream(@UserMessage String userMessage);
}
