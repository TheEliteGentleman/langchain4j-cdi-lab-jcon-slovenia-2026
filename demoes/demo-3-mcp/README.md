# Demo 3 - Hnefatafl at the Grand Thing with MCP

Third demo for JCON Slovenia: play **Hnefatafl** (Nordic runestone game) against an AI that uses the **MCP** (Model Context Protocol) to manage dice rolls.

## Overview

1. A **standalone MCP server** (`mcp-server/`) exposes a dice-rolling tool (`roll` for 2d6) via Streamable HTTP
2. A **Jarl AI agent** (`HnefataflJarlAI`) connects to this server via `McpToolProvider`
3. The agent embodies Ragnar the Skald at the Great Thing: it casts runes via MCP, applies the Hnefatafl rules, and announces the warrior's fate
4. A **Viking-themed web interface** allows for real-time gameplay

**Key message**: "MCP is the JDBC of AI - your Jakarta EE agents communicate with any external tool server"

## Prerequisites

- **Java 21+**, **Maven 3.8+**
- **Ollama** (local) or a **Mistral AI API key** (remote)

```bash
# Option A : Ollama (local)
ollama pull ministral-3:3b
ollama serve

# Option B : Mistral AI (remote)
export MISTRAL_API_KEY=your-key-here # Linux or MacOS
set MISTRAL_API_KEY=your-key-here # Windows
```

## Hnefatafl Rules (Runestone Throwing)

Hnefatafl uses two six-sided runestones (2d6):

**Opening Throw (first throw of a turn):**
- **7 or 11**: Odin's Favor - the warrior **WINS** immediately!
- **2, 3, or 12**: Norn Curse - the warrior **LOSES** immediately!
- **Any other number** (4, 5, 6, 8, 9, 10): this number becomes the **Marked Rune**

**Rune Phase (if a rune has been marked):**
- The warrior continues throwing
- If they re-roll the **Marked Rune**: they **WIN**!
- If they roll a **7**: Ragnarök - they **LOSE**!
- Any other number: no decision, re-roll

## Project Structure

```
demo-3-mcp/
├── pom.xml                               # POM aggregator
├── mcp-server/                           # MCP dice rolling server (standalone JAR)
│   ├── pom.xml                           # Helidon 4 + langchain4j-cdi-mcp-server
│   └── src/main/java/org/acme/
│       └── DiceRoller.java               # @Tool: roll(numberOfDice) -> dice results
│
├── base/                                 # Skeleton for live coding
│   ├── pom.xml
│   ├── src/main/java/com/example/demo3/
│   │   ├── JaxRsActivator.java
│   │   ├── HnefataflJarlAI.java          # TODO: @RegisterAIService + @SystemMessage
│   │   └── GameResource.java             # TODO: @Inject + call the agent
│   └── src/main/webapp/
│       ├── WEB-INF/beans.xml
│       └── index.html                    # Viking interface (ready!)
│
└── solution/                             # Complete reference implementation
    ├── pom.xml
    ├── src/main/java/com/example/demo3/
    │   ├── HnefataflJarlAI.java          # Complete: Ragnar the Skald, all the rules
    │   ├── ChatMemoryProviderBean.java   # Session report
    │   ├── LastDiceRollChatMemory.java   # Followed by the marked rune
    │   └── GameResource.java             # Complete
    └── src/main/webapp/
        ├── WEB-INF/beans.xml
        └── index.html                    # Viking interface
```

## Getting Started

### Step 1: Compile the MCP server

```bash
cd demo-project/demo-3-mcp/mcp-server
mvn clean package
```

This produces `target/casino-dice-roller.jar`. The server exposes the `roll` tool as a streamable HTTP on port 8090.

You can now start it manually:

```bash
java -jar target/casino-dice-roller.jar
```

### Step 2: Launch the app

```bash
cd demo-project/demo-3-mcp/base    # or solution/
mvn clean install
mvn liberty:dev -e
```

The application is available at **http://localhost:9080/**

### Verification

```bash
# Application Health
curl http://localhost:9080/api/game/health

# Start a Game Directly
curl http://localhost:9080/api/game/start
```

## Live Coding Guide

### Step 1: Understanding the MCP Dice Server

Examine `DiceRoller.java` - a simple CDI bean annotated `@Tool` that rolls N dice via `java.util.Random`:

```java
@ApplicationScoped
public class DiceRoller {

    @Tool(description = "Roll a number of dice and return the results.")
    public String roll(@ToolArg(description = "The number of dice") int numberOfDice) {
        int[] result = new int[numberOfDice];
        for (int i = 0; i < numberOfDice; i++) {
            result[i] = new Random().nextInt(1, 7);
        }
        return Arrays.toString(result);
    }
}
```

The `langchain4j-cdi-mcp-server` framework exposes this tool in JSON-RPC 2.0 via Streamable HTTP - no HTTP server to write.

## Step 2: Annotate HnefataflJarlAI

Open `HnefataflJarlAI.java` and add `@RegisterAIService` with `toolProviderName = "mcp"`:

```java
import dev.langchain4j.cdi.spi.RegisterAIService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

@RegisterAIService(chatModelName = "mistral", toolProviderName = "mcp")
public interface HnefataflJarlAI {

    @SystemMessage("""
		You are Ragnar the Skald, the Jarl who leads the Hnefatafl at the Great Thing of the Northern Warriors.
		
		RULES OF THE HNEFATAFL:
		- Roll 2 runestones with a roll(numberOfDice=2).
		- Opening roll: 7 or 11 → Odin's Favor (WIN)!
		2, 3, or 12 → Norn's Curse (LOSE)!
		Other → this total becomes the Marked Rune.
		- Rune Phase: Roll again until you reach the Marked Rune (WIN) or a 7 (LOSE).
		
		MANDATORY FORMAT for each roll:
		RUNES: [X, Y]
		TOTAL: [sum]
		FATE: [what happened]
		
		Answer in English, be concise, Norse expressions welcome!
        """)
    String play(@UserMessage String playerAction);
}
```

### Step 3: Connect the REST endpoint

Open `GameResource.java` and inject the agent:

```java
@Inject
HnefataflJarlAI gameMaster;

@POST @Path("/play")
@Consumes(MediaType.TEXT_PLAIN) @Produces(MediaType.TEXT_PLAIN)
public String play(String playerAction) {
    return gameMaster.play(playerAction);
}

@GET @Path("/start")
@Produces(MediaType.TEXT_PLAIN)
public String start() {
    return gameMaster.play("Salve! I'm ready to play Hnefatafl.");
}
```

### Step 4: Configure the MCP model and transport

Uncomment in `microprofile-config.properties`:

```properties
# AI Model (Option A: Mistral AI)
dev.langchain4j.cdi.plugin.mistral.class=dev.langchain4j.model.mistralai.MistralAiChatModel
dev.langchain4j.cdi.plugin.mistral.config.api-key=${MISTRAL_API_KEY}
dev.langchain4j.cdi.plugin.mistral.config.model-name=mistral-small-latest

# AI Model (Option B: Ollama)
# dev.langchain4j.cdi.plugin.mistral.class=dev.langchain4j.model.ollama.OllamaChatModel
# dev.langchain4j.cdi.plugin.mistral.config.base-url=http://localhost:11434
# dev.langchain4j.cdi.plugin.mistral.config.model-name=ministral-3:3b

# MCP Transport (Streamable HTTP -> dice server)
dev.langchain4j.cdi.plugin.ssetransport.class=dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport
dev.langchain4j.cdi.plugin.ssetransport.config.url=http://localhost:8090/mcp

# Client MCP
dev.langchain4j.cdi.plugin.mcpclient.class=dev.langchain4j.mcp.client.DefaultMcpClient
dev.langchain4j.cdi.plugin.mcpclient.config.transport=lookup:@ssetransport

# Tool Provider (named "mcp" for @RegisterAIService)
dev.langchain4j.cdi.plugin.mcp.class=dev.langchain4j.mcp.McpToolProvider
dev.langchain4j.cdi.plugin.mcp.config.mcpClients=lookup:@mcpclient
```

### Step 5: Play

Open **http://localhost:9080/** and play:

**To cast runes:**
- `Cast runes`
- `Throw`
- `New game`

**To continue (rune phase):**
- `Re-roll`
- `Continue`

## Execution Flow

```
Browser -> GET /api/game/start
	-> HnefataflJarlAI.play("Hello! I'm ready to play Hnefatafl.")
	-> The LLM decides to call roll(numberOfDice=2)
	-> McpToolProvider -> HTTP JSON-RPC -> MCP Server (port 8090)
	-> Server rolls 2d6, returns [4, 3]
	-> The LLM receives the result and writes the response:

		RUNES: [4, 3]
		TOTAL: 7
		FATE: Odin's favor! The warrior wins!

		By the gods of the North, a 7! Victory is yours!
```

## Interaction Examples

**Warrior:** `Cast the runes`

**Ragnar the Skald:**
``` 
Skal! Let the runes decide!

RUNES: [4, 3]
TOTAL: 7
FATE: Odin's favor! The warrior wins!

By the gods of the North! Victory is yours, warrior!

```

---

**Warrior:** `New game`

**Ragnar the Skald:**
```
RUNES: [3, 5]
TOTAL: 8
FATE: The marked rune is 8. Keep casting, warrior!

```

**Warrior:** `Re-roll`

**Ragnar the Skald:**
```
RUNES: [2, 6]
TOTAL: 8
FATE: Rune hit! The warrior wins!

The rune has smiled upon you - you are worthy of Valhalla!

``

## Key MCP Points

1. **Decoupling**: The dice server is an independent process (different JVM, different language possible) - `McpToolProvider` acts as the bridge
2. **Standard Protocol**: JSON-RPC 2.0 over Streamable HTTP - any compatible MCP server can be connected
3. **Pure Configuration**: The transport, client, and tool provider are all registered via MicroProfile Config - no Java code to write in the WildFly application
4. **`lookup:@`**: The `lookup:@ssetransport` prefix in the MCP config tells LangChain4j-CDI to inject the bean named `ssetransport`

## Troubleshooting

- **MCP server does not start**: Verify that the JAR is compiled (`cd mcp-server && mvn clean package`)
- **`Connection refused` On port 8090**: The MCP server is not running - restart `java -jar mcp-server/target/casino-dice-roller.jar`
- **The agent is not responding**: Verify that `HnefataflJarlAI` is annotated with `@RegisterAIService`
- **Runs are not running**: Check the WildFly logs for MCP calls (`logRequests=true` in the config)
- **Run the solution directly**: `cd solution && mvn clean install` then `mvn liberty:dev -e` 

## Resources

- **MCP Protocol**: https://modelcontextprotocol.io
- **LangChain4j-CDI**: https://github.com/langchain4j/langchain4j-cdi
- **LangChain4j MCP**: https://docs.langchain4j.dev/integrations/mcp
- **Open Liberty**: https://openliberty.io/
- **WildFly**: https://www.wildfly.org
