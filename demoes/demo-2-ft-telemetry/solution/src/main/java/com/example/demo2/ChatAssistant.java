package com.example.demo2;

import dev.langchain4j.cdi.spi.RegisterAIService;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import org.eclipse.microprofile.faulttolerance.*;
import java.time.temporal.ChronoUnit;

/**
 * AI Service for the Viking expedition assistant.
 *
 * Demonstrates:
 * - Tools: ExpeditionTools (enrollment, cancellation, search)
 * - Memory: conversation per session via @MemoryId
 * - Fault Tolerance: @Retry, @Timeout, @Fallback, @CircuitBreaker
 * - Business rules in @SystemMessage (simplified RAG)
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@RegisterAIService(chatModelName = "my-model",
                   chatMemoryProviderName = "my-memory",
                   contentRetrieverName = "my-rag",
                   tools = ExpeditionTools.class)
public interface ChatAssistant {

    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(value = 30, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "chatFallback")
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.5)
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

    /**
     * Fallback when the LLM is unavailable.
     */
    default String chatFallback(String sessionId, String message) {
        return "Oops! The LLM is taking a nap. Please try again in a moment. "
             + "(Circuit breaker active -- we're protecting your tokens!)";
    }
}
