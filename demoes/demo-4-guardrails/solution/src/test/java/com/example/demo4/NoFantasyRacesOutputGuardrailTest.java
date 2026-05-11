package com.example.demo4;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class NoFantasyRacesOutputGuardrailTest {

    private NoFantasyRacesOutputGuardrail guardrail;

    @BeforeEach
    void setUp() {
        guardrail = new NoFantasyRacesOutputGuardrail();
    }

    @Test
    void songOfNormalVikingAreAccepted() {
        String chant = """
        		O warriors of the North, raise your axes to the sky!
        		Thor guides you, Odin protects you in battle.
        		Chorus: Valhalla awaits you, son of the icy seas!
                """;

        OutputGuardrailResult result = guardrail.validate(AiMessage.from(chant));

        assertEquals(GuardrailResult.Result.SUCCESS, result.result());
        assertTrue(result.failures().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"dwarf", "dwarves"})
    void songMentioningDwarvesAreBlocked(String motInterdit) {
        OutputGuardrailResult result = guardrail.validate(
                AiMessage.from("The " + motInterdit + " forged the swords of the Viking warriors"));

        assertEquals(GuardrailResult.Result.FAILURE, result.result());
        assertFalse(result.failures().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"elf", "elves"})
    void songMentioningElvesAreBlocked(String motInterdit) {
        OutputGuardrailResult result = guardrail.validate(
                AiMessage.from("The warriors fought the " + motInterdit + " of the forest"));

        assertEquals(GuardrailResult.Result.FAILURE, result.result());
        assertFalse(result.failures().isEmpty());
    }

    @Test
    void insensitiveInformationBreaks() {
        OutputGuardrailResult result = guardrail.validate(
                AiMessage.from("The ELVES and DWARVES allied against the Vikings"));

        assertEquals(GuardrailResult.Result.FAILURE, result.result());
    }

    @Test
    void forbiddenWordInASentenceIsBlocked() {
        OutputGuardrailResult result = guardrail.validate(
                AiMessage.from("A dwarf emerged from the dark mountain"));

        assertEquals(GuardrailResult.Result.FAILURE, result.result());
    }

    @Test
    void errorMessageIsInEnglish() {
        OutputGuardrailResult result = guardrail.validate(
                AiMessage.from("The elves sang with the warriors."));

        assertFalse(result.failures().isEmpty());
        String message = result.failures().get(0).message();
        assertTrue(message.contains("dwarves") || message.contains("elves"),
                "The error message should mention fantasy races: " + message);
    }
}
