package com.example.demo5.writer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import za.co.sindi.ai.a2a.server.A2AServerError;
import za.co.sindi.ai.a2a.server.agentexecution.AgentExecutor;
import za.co.sindi.ai.a2a.server.agentexecution.RequestContext;
import za.co.sindi.ai.a2a.server.events.EventQueue;
import za.co.sindi.ai.a2a.server.tasks.TaskUpdater;
import za.co.sindi.ai.a2a.types.JSONRPCErrorResponse.JSONRPCError;
import za.co.sindi.ai.a2a.utils.Tasks;
import za.co.sindi.ai.a2a.types.InternalError;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Part;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskNotCancelableError;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.TextPart;

/**
 * CDI producer of the A2A AgentExecutor for the Creative Writer.
 * The executor receives A2A requests and delegates them to the CreativeWriter AI service.
 * 
 * TODO: To complete:
 * 1. Implement the AgentExecutor's execute() method
 * 2. Extract the text from the incoming A2A message
 * 3. Call creativeWriter.generateStory() with the extracted text
 * 4. Create a TextPart with the response and add it as an artifact
 */
@ApplicationScoped
public class CreativeWriterExecutor implements AgentExecutor {

	@Inject
    private CreativeWriter creativeWriter;

    public CreativeWriterExecutor(CreativeWriter CreativeWriter) {
        this.creativeWriter = CreativeWriter;
    }

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

        // TODO STEP 2: Extract the text from the incoming A2A message
        // String userMessage = extractTextFromMessage(context.getMessage());

        // TODO STEP 3: Call the CreativeWriter AI service
        // String response = creativeWriter.generateStory(userMessage);

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
}
