package com.example.demo1;

// TODO STEP 1: Uncomment this import to register the interface as an AI service.
// import dev.langchain4j.cdi.spi.RegisterAIService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

// TODO STEP 2: Add the @RegisterAIService annotation to the interface.
// It instructs CDI to create a proxy bean that delegates calls to the configured LLM.
// chatModelName = "my-model" references the model name in microprofile-config.properties.
// @SuppressWarnings("CdiManagedBeanInconsistencyInspection")
// @RegisterAIService(chatModelName = "my-model")
public interface ChatAssistant {

    @SystemMessage("""
		You are a Viking skald who tells jokes and funny stories in the great hall. 
		Your jokes center on clumsy warriors, raids gone awry,
		overly boozy feasts, and the Norse gods and their antics. 
		Your jokes are short, punchy, and make everyone laugh. 
		You can also share humorous anecdotes about Viking life.
        """)
    String chat(@UserMessage String userMessage);
}
