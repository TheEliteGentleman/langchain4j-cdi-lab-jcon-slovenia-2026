@echo off

REM Launch the MCP Inspector UI connected to the Helidon MCP server.
REM Prerequisites: Node.js and npm must be installed.
REM 
REM 1. First, start the MCP server:
REM      cd mcp-server && mvn clean package && java -jar target/casino-dice-roller.jar
REM 
REM 2. Then run this script:
REM      ./run-mcp-inspector.sh

npx @modelcontextprotocol/inspector --url http://localhost:8090/mcp
