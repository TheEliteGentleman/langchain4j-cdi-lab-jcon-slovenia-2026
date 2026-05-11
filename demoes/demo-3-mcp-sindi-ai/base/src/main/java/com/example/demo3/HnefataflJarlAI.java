package com.example.demo3;

// TODO: Importer les annotations LangChain4j nécessaires
// import dev.langchain4j.cdi.spi.RegisterAIService;
// import dev.langchain4j.service.SystemMessage;
// import dev.langchain4j.service.UserMessage;

/**
 * TODO: An AI agent that hosts a game of Hnefatafl at the Great Thing of the Northern Warriors. 
 *
 * To complete:
 * 1. Annotate with @RegisterAIService(chatModelName = "mistral", toolProviderName = "mcp")
 * 2. Define the play() method using @SystemMessage and @UserMessage
 * 3. The @SystemMessage must describe the role of the Jarl and the rules of Hnefatafl
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
// TODO: Add the @RegisterAIService annotation with chatModelName and toolProviderName.
public interface HnefataflJarlAI {

    // TODO: Add the @SystemMessage annotation with the Jarl of the Thing's prompt.
	// Hint: Ragnar the Skald, "The Great Thing," Hnefatafl rules.
	// IMPORTANT: Each roll must be displayed in the following format:
	// RUNES: [X, Y]
	// TOTAL: [sum]
	// FATE: [what happened]
	// He rolls using roll(numberOfDice=2)
    String play(/* TODO: Add the @UserMessage annotation */ String playerAction);
}
