package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import za.co.sindi.ai.mcp.server.spi.Tool;
import za.co.sindi.ai.mcp.server.spi.ToolArgument;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

@ApplicationScoped
public class DiceRoller {

    private static final Logger logger = Logger.getLogger(DiceRoller.class.getName());

    @Tool(description = "Rolls a number of dice and returns the results.")
    public String roll(@ToolArgument(description = "The number of dice") int numberOfDice) {
        logger.info("Dice Roll: " + numberOfDice + " dice.");
        int[] result = new int[numberOfDice];
        for (int i = 0; i < numberOfDice; i++) {
            result[i] = new Random().nextInt(1, 7);
            logger.info("Dice " + i + " : " + result[i]);
        }
        return Arrays.toString(result);
    }
}
