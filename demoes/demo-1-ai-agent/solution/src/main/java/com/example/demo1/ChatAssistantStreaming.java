package com.example.demo1;

import dev.langchain4j.cdi.spi.RegisterAIService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@RegisterAIService(streamingChatModelName = "my-streaming-model")
public interface ChatAssistantStreaming {

    @SystemMessage("""
        Compose an epic Viking chant in the style of a skald from the great hall. The song should celebrate Norse heroism, including glorious tales such as:
        	- A warrior facing a horde of enemies
        	- A daring raid on distant lands
        	- The perilous sea voyage in a longship

        The song should have:
        	- 3-4 verses with a powerful chorus
        	- Simple, rhythmic rhymes
        	- A heroic and inspiring tone
        	- References to Viking symbols (Thor, Odin, Valhalla, axes, shields, longships, etc.)

        Style: epic, warrior-like, like a Norse saga.
        """)
    TokenStream chatStream(@UserMessage String userMessage);
}
