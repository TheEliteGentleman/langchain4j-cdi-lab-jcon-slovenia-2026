package com.example.demo4;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.InputGuardrailResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class NoFantasyRacesInputGuardrailTest {

    private NoFantasyRacesInputGuardrail guardrail;

    @BeforeEach
    void setUp() {
        guardrail = new NoFantasyRacesInputGuardrail();
    }

    @Test
    void requeteNordiqueNormaleEstAcceptee() {
        InputGuardrailResult result = guardrail.validate(
                UserMessage.from("Sing of the exploits of Erik the Red and his Viking warriors"));

        assertEquals(GuardrailResult.Result.SUCCESS, result.result());
        assertTrue(result.failures().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"dwarf", "dwarfs"})
    void requeteMentionnantDesNainsEstBloquee(String motInterdit) {
        InputGuardrailResult result = guardrail.validate(
                UserMessage.from("A song with " + motInterdit + " forgers"));

        assertEquals(GuardrailResult.Result.FAILURE, result.result());
        assertFalse(result.failures().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"elf", "elves"})
    void requeteMentionnantDesElfesEstBloquee(String motInterdit) {
        InputGuardrailResult result = guardrail.validate(
                UserMessage.from("A song about the " + motInterdit + " of the forests"));

        assertEquals(GuardrailResult.Result.FAILURE, result.result());
        assertFalse(result.failures().isEmpty());
    }

    @Test
    void verificationInsensibleALaCasse() {
        InputGuardrailResult result = guardrail.validate(
                UserMessage.from("Sing of the Dwarves and the Elves"));

        assertEquals(GuardrailResult.Result.FAILURE, result.result());
    }

    @Test
    void motInterditDansUnePhraseEstBloque() {
        InputGuardrailResult result = guardrail.validate(
                UserMessage.from("I want a song about dwarves!"));

        assertEquals(GuardrailResult.Result.FAILURE, result.result());
    }

    @Test
    void messageDerreurEstEnFrancais() {
        InputGuardrailResult result = guardrail.validate(
                UserMessage.from("A song about elves"));

        assertFalse(result.failures().isEmpty());
        String message = result.failures().get(0).message();
        assertTrue(message.contains("dwarves") || message.contains("elves"),
                "The error message should mention fantasy races: " + message);
    }
}
