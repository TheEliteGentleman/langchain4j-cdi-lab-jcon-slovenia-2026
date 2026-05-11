package com.example.demo3;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * REST entry point for Hnefatafl at the Grand Thing. 
 *
 * <p>Delegates all game logic to {@link HnefataflJarlAI}. Three endpoints are exposed
 * under {@code /api/game}:
 * <ul>
 *   <li>{@code GET  /start} - welcome the warrior and start a new session.</li>
 *   <li>{@code POST /play}  - send a warrior's action (plain text) and receive the Jarl's response.</li>
 *   <li>{@code GET  /health} - availability check.</li> 
 * </ul>
 */
@Path("/game")
@ApplicationScoped
public class GameResource {

    @Inject
    HnefataflJarlAI gameMaster;

    /**
     * Sends a warrior's action to the Jarl and receives the game response.
     *
     * <p>Example: {@code POST /api/game/play} with the body {@code Cast the runes}
     *
     * <p>The Jarl will invoke the MCP {@code roll} tool to cast 2 runestones, then
     * announce the result (Odin's Favor, Curse, Marked Rune, Hit, or Ragnarök). 
     */
    @POST
    @Path("/play")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String play(String playerAction) {
        return gameMaster.play(playerAction);
    }

    /**
     * Welcomes the warrior and starts a new game session.
     */
    @GET
    @Path("/start")
    @Produces(MediaType.TEXT_PLAIN)
    public String start() {
        return gameMaster.play("Salve! I am ready to play Hnefatafl.");
    }

    /**
     * Availability check endpoint
     */
    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "The Great Thing is open - Ragnar the Skald is ready for a game of Hnefatafl!";
    }
}
