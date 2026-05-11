package com.example.demo5.scorer;

import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.agentexecution.RequestContext;
import io.a2a.server.events.EventQueue;
import io.a2a.server.tasks.TaskUpdater;
import io.a2a.spec.JSONRPCError;
import io.a2a.spec.Message;
import io.a2a.spec.Part;
import io.a2a.spec.Task;
import io.a2a.spec.TaskNotCancelableError;
import io.a2a.spec.TaskState;
import io.a2a.spec.TextPart;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import java.util.List;

/**
 * CDI Producer of the A2A AgentExecutor for the Style Scorer.
 * The executor receives A2A requests and delegates them to the StyleScorer AI service.
 * 
 * TODO: To be completed:
 * 1. Implement the execute() method of the AgentExecutor
 * 2. Extract the arguments (story, style) from the incoming A2A message
 * 3. Call styleScorer.scoreStyle() with the extracted arguments
 * 4. Create a TextPart with the score and add it as an artifact
 */
@ApplicationScoped
public class StyleScorerExecutorProducer {

    @Inject
    StyleScorer styleScorerAgent;

    @Produces
    public AgentExecutor agentExecutor() {
        return new StyleScorerExecutor(styleScorerAgent);
    }

    private static class StyleScorerExecutor implements AgentExecutor {

        private final StyleScorer styleScorer;

        public StyleScorerExecutor(StyleScorer StyleScorer) {
            this.styleScorer = StyleScorer;
        }

        @Override
        public void execute(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
            TaskUpdater updater = new TaskUpdater(context, eventQueue);

            // TODO STEP 1: Mark the task as submitted and begin work
            // if (context.getTask() == null) {
            //     updater.submit();
            // }
            // updater.startWork();

            // TODO STEP 2: Extract the arguments (story and style) from the A2A message
            // List<String> args = extractArguments(context.getMessage());

            // TODO STEP 3: Call the StyleScorer AI service
            // String response = "" + styleScorer.scoreStyle(args.get(0), args.get(1));

            // TODO STEP 4: Create the answer and complete the task
            // TextPart responsePart = new TextPart(response, null);
            // List<Part<?>> parts = List.of(responsePart);
            // updater.addArtifact(parts, null, null, null);
            // updater.complete();
        }

        @Override
        public void cancel(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
            Task task = context.getTask();

            if (task.getStatus().state() == TaskState.CANCELED) {
                throw new TaskNotCancelableError();
            }

            if (task.getStatus().state() == TaskState.COMPLETED) {
                throw new TaskNotCancelableError();
            }

            TaskUpdater updater = new TaskUpdater(context, eventQueue);
            updater.cancel();
        }

        private List<String> extractArguments(Message message) {
            if (message.getParts() != null) {
                return message.getParts().stream()
                        .filter(TextPart.class::isInstance)
                        .map(TextPart.class::cast)
                        .map(TextPart::getText)
                        .toList();
            }
            return List.of();
        }
    }
}
