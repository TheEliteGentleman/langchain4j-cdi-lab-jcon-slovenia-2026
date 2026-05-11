package com.example.demo3;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.memory.ChatMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A ChatMemory that retains the last two rune-casting exchanges. 
 *
 * The model always sees:
 *   [SystemMessage]
 *   + [Previous UserMessage] + [AiMessage (Tool Call)] + [ToolResult] + [AiMessage (Response)]
 *   + [Current UserMessage] + ... (in progress)
 *
 * This allows the LLM to compare the result of the current cast with the previous one. 
 * Eviction is triggered only when a third UserMessage arrives,
 * at which point the oldest exchange is removed. 
 */
public class LastDiceRollChatMemory implements ChatMemory {

    private static final Logger LOG = Logger.getLogger(LastDiceRollChatMemory.class.getName());

    private final Object id;
    private final List<ChatMessage> messages = new ArrayList<>();

    public LastDiceRollChatMemory(Object id) {
        this.id = id;
        LOG.fine("[Memory:%s] created".formatted(id));
    }

    @Override
    public Object id() {
        return id;
    }

    @Override
    public void add(ChatMessage message) {
    	LOG.fine("[Memory:%s] adding message type=%s".formatted(id, message.type()));
        messages.add(message);
        evict();
    }

    /**
     * Retains the SystemMessage (if present) plus the last two exchanges (previous turn + current turn). 
     * Eviction is triggered only when a third UserMessage is present. 
     * Called after every add() to ensure the window size is always maintained. 
     */
    private void evict() {
        // Retrieve the indices of all USER messages.
        List<Integer> userIndices = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).type() == ChatMessageType.USER) {
                userIndices.add(i);
            }
        }

        // Keep up to 2 exchanges: nothing to evict with <= 2 user messages.
        if (userIndices.size() <= 2) {
            return;
        }

        // Cut starting from the second-to-last UserMessage so that the model sees:
        //   previous exchange (turn N-1) + current exchange (turn N)
        int keepFromIndex = userIndices.get(userIndices.size() - 2);

        // Find the SystemMessage at the beginning (there is at most one).
        ChatMessage systemMessage = null;
        if (messages.get(0).type() == ChatMessageType.SYSTEM) {
            systemMessage = messages.get(0);
        }

        List<ChatMessage> retained = new ArrayList<>();
        if (systemMessage != null) {
            retained.add(systemMessage);
        }
        retained.addAll(messages.subList(keepFromIndex, messages.size()));

        LOG.fine("[Memory:%s] Evicting oldest batch: %d messages retained (%d deleted)"
        		.formatted(id, retained.size(), messages.size() - retained.size()));
        messages.clear();
        messages.addAll(retained);
    }

    @Override
    public List<ChatMessage> messages() {
        return List.copyOf(messages);
    }

    @Override
    public void clear() {
    	LOG.fine("[Memory:%s] cleared".formatted(id));
        messages.clear();
    }
}
