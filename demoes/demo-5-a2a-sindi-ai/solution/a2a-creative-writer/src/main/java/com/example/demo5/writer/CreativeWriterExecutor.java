package com.example.demo5.writer;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import za.co.sindi.ai.a2a.server.A2AServerError;
import za.co.sindi.ai.a2a.server.agentexecution.AgentExecutor;
import za.co.sindi.ai.a2a.server.agentexecution.RequestContext;
import za.co.sindi.ai.a2a.server.events.EventQueue;
import za.co.sindi.ai.a2a.server.tasks.TaskUpdater;
import za.co.sindi.ai.a2a.types.InternalError;
import za.co.sindi.ai.a2a.types.Part;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskNotCancelableError;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.TextPart;
import za.co.sindi.ai.a2a.utils.Messages;
import za.co.sindi.ai.a2a.utils.Tasks;

@ApplicationScoped
public class CreativeWriterExecutor implements AgentExecutor {

	@Inject
    private CreativeWriter creativeWriter;

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

        // mark the task as submitted and start working on it
        if (context.getTask() == null) {
            updater.submit();
        }
        updater.startWork(Messages.newAgentTextMessage("Processing request...", task.getContextId(), task.getId()));

        // extract the text from the message
        final String userMessage = context.getUserInput(); //extractTextFromMessage(context.getMessage());

        // call the creative writer agent with the user's message
        String response = creativeWriter.generateStory(userMessage);

        System.out.println("CreativeWriterExecutor: Generated response: " + response);

        // create the response part
        final TextPart responsePart = new TextPart(response);
        final List<Part> parts = List.of(responsePart);

        // add the response as an artifact and complete the task
        updater.addArtifact(parts);
        updater.complete();
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

