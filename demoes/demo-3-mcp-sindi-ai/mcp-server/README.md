# MCP Dice Server

Standalone MCP (Model Context Protocol) server for runestone casting.

## Description

This server exposes a dice-rolling tool via the MCP protocol over stdio (JSON-RPC 2.0). It is used by the `CasinoDealerAI` agent (Ragnar le Skald) to manage game mechanics (rune stone rolls).

## Tool Available

| Tool | Description | Settings |
|-------|-------------|------------|
| `roll` | Roll a number of 6-sided dice. | `numberOfDice(int)` : Number of dice |

## Compilation

```bash
cd demo-3-mcp/mcp-server
mvn clean package
```

The generated JAR is located at `target/demo-3-mcp-dice-server.jar`.

## Usage

### As an MCP server (Normal Mode)

The server is started **automatically** by the `solution` or `base` module via the `McpConfig` CDI producer. It communicates with the WildFly application via stdin/stdout.

You **do not need** to start it manually for the demo.

### Manual Test (Standalone Mode)

To test the server independently:

```bash
java -jar target/demo-3-mcp-dice-server.jar
```

Then send JSON-RPC commands via `stdin`. Examples:

**1. Initialisation**
```json
{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}
```

**2. List the tools**
```json
{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}
```

**3. Tool Call (Throw 2 Runestones)**
```json
{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"roll","arguments":{"numberOfDice":2}}}
```

**4. Roll 3 dice**
```json
{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"roll","arguments":{"numberOfDice":3}}}
```

## MCP Protocol

The server implements the MCP protocol version `2024-11-05`:
- Communication via **stdin/stdout**
- **JSON-RPC 2.0** format
- **stdio** transport (no network)

## Architecture

```
+---------------------+
|  WildFly (solution)  |
|                      |
|  +----------------+  |
|  | CasinoDealerAI |  |  The LLM decides to 
|  +-------+--------+  |  invoke the runes (tool calling).
|          |           |
|  +-------v--------+  |
|  |  McpConfig     |  |  CDI producer initiating 
|  |  (Producer)    |  |  the MCP process
|  +-------+--------+  |
+-----------+-----------+
            | stdio
            | (JSON-RPC)
+-----------v-----------+
|  MCP Dice Server      |
|  (this module)        |
|                       |
|  - roll               |  Roll N 6-sided dice 
|                       |  and return the results.
+-----------------------+
```

## Logs

Logs are sent to stderr:
```
[main] INFO org.acme.DiceRoller - Dice Roll: 2 Dice
[main] INFO org.acme.DiceRoller - Dice 0 : 4
```

## Troubleshooting

**The server is not responding**
- Verify that the JAR is correctly compiled: `ls -lh target/demo-3-mcp-dice-server.jar`
- Check the logs in the WildFly console

**"Unable to start MCP server" error**
- The path to the JAR in `McpConfig.java` is incorrect
- The JAR does not have execute permissions

**The dice are not being rolled**
- Verify that the `McpToolProvider` is correctly injected using `@Named("mcp")`
- Verify that the LLM supports tool calling (Ollama with recent models)

## Resources

- **MCP Protocol** : https://modelcontextprotocol.io
- **JSON-RPC 2.0 Specification** : https://www.jsonrpc.org/specification
