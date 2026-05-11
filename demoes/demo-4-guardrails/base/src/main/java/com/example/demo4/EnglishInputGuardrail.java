package com.example.demo4;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

/**
 * Guardrail input that only accepts requests in English.
 * Uses Apache Tika to detect the message language.
 * If the language is definitively identified as non-English, the request is rejected.
 */
@ApplicationScoped
public class EnglishInputGuardrail implements InputGuardrail {

    private LanguageDetector detector;

    @PostConstruct
    void init() {
        // TODO: Initialize the detector: new OptimaizeLangDetector().loadModels()
    }

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        // TODO: Get the user message text with userMessage.singleText()
        // TODO: Detect the language with detector.detect(text) (synchronized on detector)
        // TODO: Return failure("...") if result.isReasonablyCertain() AND language is not "en"
        // TODO: Return success() otherwise (English or too short to detect)
        return success();
    }
}
