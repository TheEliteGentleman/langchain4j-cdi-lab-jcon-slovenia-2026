  #!/bin/sh
# Launch the MCP Inspector UI connected to the Helidon MCP server.
# Prerequisites: Node.js and npm must be installed.
#
# 1. First, start the MCP server:
#      cd mcp-server && mvn clean package && java -jar target/casino-dice-roller.jar
#
# 2. Then run this script:
#      ./run-mcp-inspector.sh

npx @modelcontextprotocol/inspector --url http://localhost:8090/mcp
