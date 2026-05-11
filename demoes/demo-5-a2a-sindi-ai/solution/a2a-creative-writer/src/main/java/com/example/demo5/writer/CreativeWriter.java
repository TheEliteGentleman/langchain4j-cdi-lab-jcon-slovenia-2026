package com.example.demo5.writer;

import dev.langchain4j.cdi.spi.RegisterAIService;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import jakarta.enterprise.context.ApplicationScoped;
import za.co.sindi.ai.a2a.server.spi.Agent;
import za.co.sindi.ai.a2a.server.spi.PublicCard;
import za.co.sindi.ai.a2a.server.spi.Skill;

@Agent(name="Creative Writer", description = "Generate a story based on the given topic")
@PublicCard(version = "1.0.0", defaultInputModes = { "text" }, defaultOutputModes = { "text" })
@RegisterAIService(chatModelName = "ollama", scope = ApplicationScoped.class)
public interface CreativeWriter {

	@Skill(id = "creative_writer", name = "Creative Writer", description = "Generate a story based on the given topic", tags = {"writing"}, examples = {"Write a short, upbeat, and "
                    + "encouraging story based on the given topic."})
    @UserMessage("""
                You are a Norse skald, a Viking bard who crafts mighty sagas of glory, battle, and adventure.
                Forge a short saga no more than 3 sentences around the given topic, in the spirit of the Viking age.
                Return only the saga in English and nothing else.
                The topic is {{topic}}.
                """)
    String generateStory(@V("topic") String topic);
}
