package com.example.demo4;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.InputGuardrailResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnglishInputGuardrailTest {

    private EnglishInputGuardrail guardrail;

    @BeforeEach
    void setUp() {
        guardrail = new EnglishInputGuardrail();
        guardrail.init();
    }

    @Test
    void englishRequestIsAccepted() {
        InputGuardrailResult result = guardrail.validate(
                UserMessage.from("Sing me a song about the great Viking warrior Erik the Red and his epic battles across the northern seas."));

        assertEquals(GuardrailResult.Result.SUCCESS, result.result());
        assertTrue(result.failures().isEmpty());
    }
    
    @Test
    void frenchRequestIsBlocked() {
        InputGuardrailResult result = guardrail.validate(
                UserMessage.from("Chante les exploits d'Erik le Rouge, grand guerrier du Nord. Raconte ses batailles épiques et ses voyages vers le Vinland."));

        assertEquals(GuardrailResult.Result.FAILURE, result.result());
        assertFalse(result.failures().isEmpty());
    }

    @Test
    void failureMessageIsInEnglish() {
        InputGuardrailResult result = guardrail.validate(
                UserMessage.from("Peux-tu chanter les exploits du grand guerrier viking et ses batailles légendaires à travers les mers du Nord ?"));

        assertFalse(result.failures().isEmpty());
        String message = result.failures().get(0).message();
        assertTrue(message.contains("English"), "Le message d'erreur doit être en anglais: " + message);
    }

    @Test
    void shortOrAmbiguousTextIsAccepted() {
        // Tika cannot be confident about very short texts → benefit of the doubt
        InputGuardrailResult result = guardrail.validate(UserMessage.from("Odin"));

        assertEquals(GuardrailResult.Result.SUCCESS, result.result());
    }
}
