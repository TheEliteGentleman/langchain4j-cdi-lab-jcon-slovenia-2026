package com.example.demo2;

import dev.langchain4j.cdi.spi.RegisterAIService;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
* AI Service for the Viking Expeditions Assistant. 
* The agent already operates with Tools + Memory - now, we're adding resilience! 
*/
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@RegisterAIService(chatModelName = "my-model",
                   chatMemoryProviderName = "my-memory",
                   contentRetrieverName = "my-rag",
                   tools = ExpeditionTools.class)
public interface ChatAssistant {

	// TODO STEP 1: Add @Retry(maxRetries = 3, delay = 1000) 
	// TODO STEP 2: Add @Timeout(value = 30, unit = ChronoUnit.SECONDS) 
	// TODO STEP 3: Add @Fallback(fallbackMethod = "chatFallback") 
	// TODO STEP 4: Add @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.5)
    @SystemMessage("""        
        You are the Viking Expedition Assistant. 
        You have access to a knowledge base containing information on expeditions, leaders, and destinations. 
        Use this information to answer questions regarding expedition details, requirements, and leaders. 

        IMPORTANT - MANDATORY TOOL USAGE:
        You MUST call the appropriate tools for EVERY action. NEVER simulate an action. 
        - To list expeditions: call `listExpeditions`. 
        - To enroll a warrior: call `enrollWarrior`. NEVER state "enrollment confirmed" without having called `enrollWarrior`. 
        - To cancel an enrollment: call `cancelEnrollment`. 
        - To check remaining slots: call `remainingSlots`. 
        - To view current enrollments: call `myEnrollments`. 
        If you do not call the tool, the action DID NOT occur. 

        RULES:
        - To enroll a warrior, you require both their first name AND their last name. 
        If either is missing, ask for it. 
        - Do NOT display technical identifiers (e.g., raid-england) to the user. 
        Use them internally when calling the tools. 
        - Respond in English, and be concise.
        """)
    String chat(@MemoryId String sessionId, @UserMessage String message);

    // TODO STEP 5: Implement the fallback
    // default String chatFallback(String sessionId, String message) {
    //     return "Oops! The LLM is taking a nap. Please try again in a moment.";
    // }
}
