package com.example.demo4;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

/**
 * Guardrail input that only accepts requests in English.
 * Uses Apache Tika to detect the message language.
 * If the language is definitively identified as non-English, the request is rejected.
 */
@ApplicationScoped
@Named("english-input")
public class EnglishInputGuardrail implements InputGuardrail {

    private LanguageDetector detector;

    @PostConstruct
    void init() {
        detector = new OptimaizeLangDetector().loadModels();
    }

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText();
        LanguageResult result;
        synchronized (detector) {
            result = detector.detect(text);
        }
        if (result.isReasonablyCertain() && !"en".equals(result.getLanguage())) {
            return failure("Only English is accepted! Speak English to summon the skald.");
        }
        return success();
    }
}
