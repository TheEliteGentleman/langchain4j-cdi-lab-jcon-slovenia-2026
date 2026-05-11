package com.example.demo1;

// TODO STEP 3: Remove the four imports below - they are no longer needed with CDI.
// CDI automatically instantiates and injects ChatAssistant and ChatAssistantStreaming.
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import dev.langchain4j.model.mistralai.MistralAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;

// TODO STEP 3: Uncomment this import for CDI injection
// import jakarta.inject.Inject;
import dev.langchain4j.service.TokenStream;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

@Path("/chat")
@ApplicationScoped
public class ChatResource {

	// TODO STEP 3: Replace these two fields with CDI injections. 
	// Once ChatAssistant and ChatAssistantStreaming are annotated with @RegisterAIService,
	// CDI can inject them directly - no manual instantiation required.
    //
    // @Inject
    // ChatAssistant assistant;
    //
    // @Inject
    // ChatAssistantStreaming streamingAssistant;
    private ChatAssistant assistant;
    private ChatAssistantStreaming streamingAssistant;

	 // TODO STEP 3: Delete this entire @PostConstruct method. 
	 // With @Inject, CDI manages the bean lifecycle - they are available
	 // before the first call without any manual initialization.
    @PostConstruct
    void init() {
        MistralAiChatModel model = MistralAiChatModel.builder()
                .apiKey(System.getenv("MISTRAL_API_KEY"))
                .modelName("mistral-small-latest")
                .logRequests(true)
                .logResponses(true)
                .build();

        assistant = AiServices.builder(ChatAssistant.class)
                .chatModel(model)
                .build();

        MistralAiStreamingChatModel streamingModel = MistralAiStreamingChatModel.builder()
                .apiKey(System.getenv("MISTRAL_API_KEY"))
                .modelName("mistral-small-latest")
                .build();

        streamingAssistant = AiServices.builder(ChatAssistantStreaming.class)
                .streamingChatModel(streamingModel)
                .build();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String chat(String message) {
        return assistant.chat(message);
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void chatStream(@QueryParam("message") String message, @Context SseEventSink sseEventSink, @Context Sse sse) {
        if (message == null || message.trim().isEmpty()) {
            try (sseEventSink) {
                sseEventSink.send(sse.newEvent("error", "Message parameter is required"));
            }
            return;
        }

        try {
            TokenStream tokenStream = streamingAssistant.chatStream(message);

            tokenStream
                .onPartialResponse(token -> {
                    sseEventSink.send(sse.newEvent("token", token.replace("\n", "<br/>")));
                })
                .onCompleteResponse(response -> {
                    sseEventSink.send(sse.newEvent("done", ""));
                    sseEventSink.close();
                })
                .onError(error -> {
                    sseEventSink.send(sse.newEvent("error", error.getMessage()));
                    sseEventSink.close();
                })
                .start();

        } catch (Exception e) {
            try (sseEventSink) {
                sseEventSink.send(sse.newEvent("error", "Error: " + e.getMessage()));
            }
        }
    }

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "OK";
    }
}
