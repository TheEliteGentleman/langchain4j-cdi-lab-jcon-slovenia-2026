package com.example.demo4;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.Set;

/**
 * Guardrail output that rejects generated chants mentioning fantasy races.
 * Ensures that the LLM hasn't slipped dwarves or elves into the Viking chant.
 */
@ApplicationScoped
@Named(("fantasy-output"))
public class NoFantasyRacesOutputGuardrail implements OutputGuardrail {

    private static final Set<String> FORBIDDEN_WORDS = Set.of(
    		"dwarf", "dwarves", "elf", "elves"
    );

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        String text = responseFromLLM.text().toLowerCase();

        for (String word : FORBIDDEN_WORDS) {
            if (text.contains(word)) {
                return failure("The song of the skald should not evoke dwarves or elves! It is a pure Viking saga.");
            }
        }
        return success();
    }
}
