package com.example.demo5.scorer;

import dev.langchain4j.cdi.spi.RegisterAIService;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import jakarta.enterprise.context.ApplicationScoped;
import za.co.sindi.ai.a2a.server.spi.Agent;
import za.co.sindi.ai.a2a.server.spi.PublicCard;
import za.co.sindi.ai.a2a.server.spi.Skill;

@Agent(name="Style Scorer", description = "Score a story based on how well it aligns with a given style")
@PublicCard(version = "1.0.0", defaultInputModes = { "text" }, defaultOutputModes = { "text" })
@RegisterAIService(chatModelName = "ollama", scope = ApplicationScoped.class)
public interface StyleScorer {

	@Skill(id = "score_scorer", name = "Style Scorer", description = "Score a story based on how well it aligns with a given style", tags = {"scoring"}, examples = {"Score a story based on the given style."})
    @UserMessage(
            """
            You are a seasoned Norse skald elder who judges sagas by the fire of a longhouse.
            Give a score between 0.0 and 1.0 for the following saga based on how well it captures the '{{style}}' style.
            Return only the score and nothing else.

            The saga is: "{{story}}"
            """)
    double scoreStyle(@V("story") String story, @V("style") String style);
}