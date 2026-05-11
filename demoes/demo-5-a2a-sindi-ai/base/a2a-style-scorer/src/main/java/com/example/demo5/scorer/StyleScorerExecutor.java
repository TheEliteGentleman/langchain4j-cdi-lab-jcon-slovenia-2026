package com.example.demo5.scorer;

import java.util.Arrays;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import za.co.sindi.ai.a2a.server.A2AServerError;
import za.co.sindi.ai.a2a.server.agentexecution.AgentExecutor;
import za.co.sindi.ai.a2a.server.agentexecution.RequestContext;
import za.co.sindi.ai.a2a.server.events.EventQueue;
import za.co.sindi.ai.a2a.server.tasks.TaskUpdater;
import za.co.sindi.ai.a2a.types.InternalError;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskNotCancelableError;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.TextPart;
import za.co.sindi.ai.a2a.utils.Tasks;

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
public class StyleScorerExecutor implements AgentExecutor {

	@Inject
    private StyleScorer styleScorer;

    @Override
    public void execute(RequestContext context, EventQueue eventQueue) {
    	Task task = context.getTask();
		if (task == null) task = Tasks.newTask(context.getMessage());
		try {
			eventQueue.enqueueEvent(task);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new A2AServerError(new InternalError());
		}
		
		final TaskUpdater updater = new TaskUpdater(eventQueue, task.getId(), task.getContextId());

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
    public void cancel(RequestContext context, EventQueue eventQueue) {
    	Task task = context.getTask();

        if (task.getStatus().state() == TaskState.CANCELED) {
            // task already cancelled
            throw new A2AServerError(new TaskNotCancelableError());
        }

        if (task.getStatus().state() == TaskState.COMPLETED) {
            // task already completed
            throw new A2AServerError(new TaskNotCancelableError());
        }

        // cancel the task
        final TaskUpdater updater = new TaskUpdater(eventQueue, task.getId(), task.getContextId());
        updater.cancel();
    }

    private List<String> extractArguments(Message message) {
        if (message.getParts() != null) {
            return Arrays.stream(message.getParts())
                    .filter(TextPart.class::isInstance)
                    .map(TextPart.class::cast)
                    .map(TextPart::getText)
                    .toList();
        }
        return List.of();
    }
}
