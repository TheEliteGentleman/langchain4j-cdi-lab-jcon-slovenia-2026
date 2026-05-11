# Demo 3 - Quick Start Guide

## Quick Start (3 minutes)

### 1. Start Ollama

```bash
ollama pull ministral-3:3b
ollama serve
```

> **Note**: Leave this terminal open. Ollama must be running on `localhost:11434`..

### 2. Compile the MCP server for dice

```bash
cd demo-3-mcp/mcp-server
mvn clean package
```

Result: `target/demo-3-mcp-dice-server.jar`.

### 3. Start the application

```bash
cd ../solution/
mvn clean install
mvn liberty:dev -e
```


### 4. Play Hnefatafl!

Open `http://localhost:9080/demo-3/` — the Viking interface loads automatically.

Try these commands:

- `Throw the runes` - Ragnar casts 2 runestones to determine your destiny
- `Roll again` - Continue during the rune phase
- `New game` - Start a new round

Or via curl:

```bash
curl -X POST -H "Content-Type: text/plain" \
  -d "Throw the runes" \
  http://localhost:9080/demo-3/api/game/play
```

## What You Will See

- **Ragnar the Skald**: An AI Jarl agent of the Great Thing, officiating the Hnefatafl match
- **MCP Tool Calls**: The LLM calls `roll(numberOfDice=2)` via the MCP protocol
- **Hnefatafl Rules**: 7/11 = Odin's Favor; 2/3/12 = Curse; otherwise = Marked Rune

## How It Works

```
Warrior -> JAX-RS -> CasinoDealerAI (@RegisterAIService)
-> The LLM decides to call the 'roll' tool
-> `McpToolProvider` -> JSON-RPC -> MCP Dice Server (stdio)
-> The server rolls 2d6 -> returns the result
-> The LLM applies the rules -> responds in character
```

## Stop Everything

```bash
# in the terminal: Ctrl+C
```

## Common Issues

**"MCP server not found"**:
- Verify that the JAR has been compiled: `ls mcp-server/target/demo-3-mcp-dice-server.jar`
- Recompile if necessary: ??`cd mcp-server && mvn clean package`

**"Connection refused" on the chat**:
- Verify that Ollama is running: `curl http://localhost:11434/api/tags`
- Verify that the model has been downloaded: `ollama list`

**Port 8080 already in use**:
- Check what is using it: `lsof -i :8080`
- Or use the provisioned server with a port offset: `./target/server/bin/standalone.sh -Djboss.socket.binding.port-offset=10`

**Runes are not being cast (the LLM is fabricating the results)**:
- Check the server logs for traces of MCP tool calls
- Try a larger model (`qwen2.5:7b`) for better tool-calling accuracy
