package com.example.demo4;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;

/**
 * Guardrail output that rejects generated chants mentioning fantasy races.
 * Ensures that the LLM hasn't slipped dwarves or elves into the Viking chant.
 */
@ApplicationScoped
public class NoFantasyRacesOutputGuardrail implements OutputGuardrail {

    private static final Set<String> FORBIDDEN_WORDS = Set.of(
    		"dwarf", "dwarves", "elf", "elf", "elves"
    );

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        // TODO: Get the AI response text with responseFromLLM.text()
        // TODO: Check if it contains any FORBIDDEN_WORDS (case-insensitive)
        // TODO: Return failure("The song of the skald should not evoke dwarves or elves! It is a pure Viking saga.") if a forbidden word is found
        // TODO: Return success() otherwise
        return success();
    }
}
