# Manhunt Plugin

Allow you and your friends to set up and participate in your own Manhunt game! For those who are not familiar; the runner must kill the ender dragon while the hunters chase them. The hunters only get a compass that always points in the direction of the runner - everything else is up to them. 

## Functionality

- Add/remove players to the hunter team or the runner team.
- Start/Stop the game.
- Hunters get a compass to track the runner, updating once per second.
- View team info and change game options (currently only one option available).

Messages notify the command sender and the affected players when they are added or removed from a team, or when the game starts and stops.

## How to Install

1. **Download and Install:**
   - Download the jar file from the releases page.
   - Drag it into your server's plugin folder.

2. **Build from Source:**
   - Clone the project repository.
   - Use an IDE like IntelliJ to build the project.
   - The built jar file will be located in the `/target` directory.
   - Drag and drop the built jar file into your server's plugin folder.

## Structure

- **Core Components:**
  - **Game**: Main game logic.
  - **Command Manager**: Registers and executes commands.
  - **Plugin Options**: Manages plugin configuration.

- **Commands:**
  - Commands are registered and managed by the Command Manager to allow isolated command creation and registration.
  - Commands interact with plugin options and the game itself, handling actions like starting/stopping the game, adding players, getting info, etc.
  
- **Team Management:**
  - **TeamManager**: Manages adding/removing players and enforces game logic.
  - **Teams**: Separate classes for managing the hunter and runner teams, designed to handle generic players.

- **Utilities:**
  - Messaging players.
  - Constants for commands and game options.
  - Team management classes.

  The aim is to make this plugin easy to extend and integrate into other projects, with clear distinctions between game-specific and generic utilities.
