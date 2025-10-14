# Copilot Instructions for stock-pricing-and-analysis-application

## Project Architecture
- **Backend (JettyServer/):** Java 8+ server using Jetty 12 for WebSocket streaming of real-time stock data from Alpaca API.
- **Client (TestingClient/):** Node.js client for connecting and testing WebSocket endpoints.
- **Key Classes:**
  - `MarketDataStreamer.java`: Handles Alpaca API integration and data streaming logic.
  - `MyServer.java`: Jetty WebSocket server entrypoint.
  - `MyEndpoint.java`: WebSocket endpoint for client connections.
  - `AlpacaEndpoint.java`: (if present) likely for Alpaca-specific WebSocket logic.

## Data Flow & Patterns
- **WebSocket:** Clients connect to `MyEndpoint` for real-time updates.
- **Observer Pattern:** Used for modular event handling and client subscription (see `MarketDataStreamer`).
- **Environment Variables:** `ALPACA_API_KEY` and `ALPACA_SECRET` must be set for API access.

## Build & Run
- **Manual Build:**
  - Compile: `javac MyServer.java` (from JettyServer/src/main/java/com/example)
  - Run: `java MyServer`
- **Maven:**
  - Use `mvn package` in `JettyServer/` for full build.
- **Node Client:**
  - Run `node client.js` in `TestingClient/` to test WebSocket connection.

## Conventions & Integration
- **Modular Design:** New features should use OOP and Observer pattern for extensibility.
- **External API:** All market data comes from Alpaca; integration code is in `MarketDataStreamer.java`.
- **Testing:** No formal test suite yet; use `TestingClient/client.js` for manual WebSocket testing.
- **Dependencies:**
  - Jetty 12 (see `JettyServer/pom.xml`)
  - Alpaca API (external)

## Examples
- To add a new data source, create a new streamer class and register it with the server using Observer pattern.
- To extend client functionality, update `client.js` in `TestingClient/`.

## Key Files
- `JettyServer/src/main/java/com/example/MarketDataStreamer.java`
- `JettyServer/src/main/java/com/example/MyServer.java`
- `JettyServer/src/main/java/com/example/MyEndpoint.java`
- `TestingClient/client.js`
- `JettyServer/pom.xml`

---
For questions or unclear conventions, review the README or ask for clarification. Please suggest improvements if you find missing or outdated instructions.