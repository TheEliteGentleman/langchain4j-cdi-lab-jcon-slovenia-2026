package com.example.demo4;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

/**
 * Guardrail output that verifies that the generated song is in English.
 * Uses Apache Tika to confirm the language of the LLM response.
 * Since the response is long enough, the detection is reliable.
 */
@ApplicationScoped
@Named("english-output")
public class EnglishOutputGuardrail implements OutputGuardrail {

    private LanguageDetector detector;

    @PostConstruct
    void init() {
        detector = new OptimaizeLangDetector().loadModels();
    }

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        String text = responseFromLLM.text();
        LanguageResult result;
        synchronized (detector) {
            result = detector.detect(text);
        }
        if (!result.isReasonablyCertain() || !"en".equals(result.getLanguage())) {
            return failure("The song must be in English! The skald must sing in the language of Molière.");
        }
        return success();
    }
}
