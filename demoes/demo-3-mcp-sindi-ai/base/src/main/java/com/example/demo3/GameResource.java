package com.example.demo3;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * TODO: REST entry point for playing Hnefatafl at the Viking Grand Thing. 
 *
 * To complete:
 * 1. Inject the CasinoDealerAI.
 * 2. Implement the play() method, which calls the agent.
 */
@Path("/game")
@ApplicationScoped
public class GameResource {

    // TODO: Inject CasinoDealerAI with @Inject.
    // CasinoDealerAI gameMaster;

    /**
     * TODO: Perform an action in the game of Hnefatafl.
     */
    @POST
    @Path("/play")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String play(String playerAction) {
        // TODO: Appeler gameMaster.play(playerAction)
        throw new UnsupportedOperationException("TODO: To implement during live coding");
    }

    /**
     * Enter the Thing and start a game.
     */
    @GET
    @Path("/start")
    @Produces(MediaType.TEXT_PLAIN)
    public String start() {
        // TODO: Retourner gameMaster.play("Salve! I am ready to play Hnefatafl.")
        throw new UnsupportedOperationException("TODO: To implement during live coding");
    }

    /**
     * Availability check endpoint.
     */
    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "The Great Thing is open - Ragnar the Skald is ready for a Hnefatafl!";
    }
}
