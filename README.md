# Static GUI Game Project
This project submission includes a static GUI for a basic game. All necessary screens, visual elements, and screen transitions are implemented to meet the assignment requirements. No functional event handlers are included, as per the project requirements for this phase.

## Project Overview
The project includes the following core screens and components:

Home Page: The main menu screen that serves as the entry point.
Menus: Includes options for Load, Save, Pause, and Level Selection.
Game Screen: Displays the main gameplay interface with characters and obstacles.
In-Game Components: Static elements such as the Catapult, Birds (Red, Yellow), Pigs, and Blocks.
Pause/Level End Screens: Shows the game status, including Win, Lose, and Pause screens.
Each screen has been designed as a static GUI with visual elements only, without functional event handlers in line with the assignment requirements.

## Setup and Execution
To set up and run the project, follow these instructions:

## Prerequisites
Java (JDK 8+)
Gradle
Steps
Clone the Repository

bash
Copy code
git clone <repository-url>
cd <repository-directory>
Build the Project

bash
Copy code
gradle build
Run the Project

bash
Copy code
gradle run
This will launch the static GUI, allowing you to navigate between screens using the available events.

## Testing the Project
Since this is a static GUI without event handlers, functional testing is not required. However, you can manually navigate the interface to ensure each screen loads correctly.

## Navigation and Screen Transitions
The following key bindings are used to transition between screens:

O Key: Navigates to the Win Screen.
P Key: Navigates to the Lose Screen.
Pause Button: Pauses the game and opens the Pause Menu Screen.
These key events facilitate navigation between the screens, meeting the requirement for screen transitions in the static GUI.

## Code Structure and Conventions
The project follows standard Java conventions for organization and readability:

Package Structure: Organized under com.test_game.main, with sub-packages for birds, blocks, levels, pigs, and screens.
Naming Conventions: Classes and variables are named using camelCase and PascalCase as per Java standards.
Assets
All static assets are located in the assets folder, including:

Images: Background, characters (birds, pigs), and other game elements.
Skin File: UI skin for buttons and other interactive components.
Resources and References
No external resources were referenced for this project. All assets and code were created specifically for this assignment.

