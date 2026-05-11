package com.example.demo4;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;

/**
 * Guardrail entry rejects requests mentioning fantasy races (dwarves or elves).
 * Vikings know neither dwarves nor elves!
 */
@ApplicationScoped
public class NoFantasyRacesInputGuardrail implements InputGuardrail {

    private static final Set<String> FORBIDDEN_WORDS = Set.of(
    		"dwarf", "dwarves", "elf", "elf", "elves"
    );

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        // TODO: Get the user message text with userMessage.singleText()
        // TODO: Check if it contains any FORBIDDEN_WORDS (case-insensitive)
        // TODO: Return failure("The Vikings know neither dwarves nor elves! Keep your request purely Nordic.") if a forbidden word is found
        // TODO: Return success() otherwise
        return success();
    }
}
