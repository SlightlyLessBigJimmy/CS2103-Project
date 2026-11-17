# Jersey Slide

Jersey Slide is a 2D highway driving game written in Java with Swing, developed for CS 2103. The project is in early development: the window, rendering pipeline, update loop and core game-object model are in place, while vehicle behaviour, collision detection and frame timing are planned but not yet implemented.

## Features

Implemented:

- A resizable application window titled "Jersey Slide", sized relative to the user's screen.
- A rendering pipeline that draws registered objects to a Swing panel, repainted approximately every 16 ms (about 60 frames per second).
- A full-window background image loaded from `images/Background.png`.
- A game-object model with position, size and orientation (`GameObject`, `Transform`, `Vector3`).
- A rigid-body component that moves its game object by a velocity each update and dispatches collision events to registered listeners.
- A global update loop that calls `update()` on every registered `GlobalUpdate` listener.
- A registry of car statistics (`CarStats`) keyed by car name.

Planned (marked `TODO` in the source):

- Vehicle lane-centering and a vehicle state machine (Driving, Panic, Damaged, Breakdown, Destroyed).
- Hitboxes and collision detection.
- Frame timing (`deltaTime` and `fixedDeltaTime`).
- Sprite rendering through the `Renderable` interface.

## Tech Stack

| Component | Detail |
|---|---|
| Language | Java (JDK 25; `Main.java` is a compact source file with an instance-free `main()`) |
| UI and graphics | Java Swing and AWT (`JFrame`, `JPanel`, `Graphics2D`) |
| Image loading | `javax.imageio.ImageIO` |
| Events | `javax.swing.event.EventListenerList` |
| IDE configuration | IntelliJ IDEA module (`CS2103 project.iml`) |
| External dependencies | None |

## Project Structure

```
CS2103-Project/
├── images/
│   └── Background.png        Background image for the game environment (2525 × 1248)
├── src/
│   ├── Main.java             Entry point: builds the window, starts the repaint timer and the update loop
│   ├── GameWindow.java       Application window (JFrame) titled "Jersey Slide"
│   ├── GamePanel.java        Swing panel that delegates painting to the renderer
│   ├── RendererDef.java      Holds Renderable objects and draws them each frame
│   ├── Renderable.java       Interface for anything that can be drawn
│   ├── Environment.java      Renderable background that draws Background.png
│   ├── Sprite.java           Wrapper around a single loaded image
│   ├── GameObject.java       Base class for objects in the game world
│   ├── Transform.java        Position, size and orientation of a GameObject
│   ├── Vector3.java          Three-component vector with sum and magnitude
│   ├── RigidBody.java        Velocity-based movement and collision events (includes CollisionEvent)
│   ├── CollisionListener.java Listener interface for collision events
│   ├── GlobalUpdate.java     Interface for objects updated every loop iteration
│   ├── Timing.java           Static frame-timing values (not yet calculated)
│   ├── Vehicle.java          Abstract base class for vehicles
│   ├── VehicleState.java     Vehicle states and their intended transitions
│   ├── Cars.java             Registry of car statistics by name
│   ├── CarStats.java         Record of a car's durability and hitbox
│   └── Hitbox.java           Placeholder for collision bounds
├── .idea/                    IntelliJ IDEA project settings
└── CS2103 project.iml        IntelliJ IDEA module file
```

## Getting Started

### Prerequisites

- JDK 25 or later. The IntelliJ project is configured for `openjdk-25`. Earlier JDKs cannot compile `Main.java` without preview flags, because it uses the compact source file syntax.

### Running in IntelliJ IDEA

1. Open the `CS2103-Project` folder in IntelliJ IDEA.
2. Confirm the project SDK is set to JDK 25 (File → Project Structure → Project).
3. Run `Main.java`. The working directory must be the project root so that `images/Background.png` can be found.

### Running from the command line

From the project root:

```bash
javac -d out src/*.java
java -cp out Main
```

The game must be launched from the project root. The background image is loaded by the relative path `images/Background.png`; launching from another directory prints `File not found in: <directory>` and the background is not drawn.

## Usage

Launching the game opens the "Jersey Slide" window with the background environment rendered. Closing the window exits the application. Gameplay and controls are not yet implemented.

## Architecture

The application is organised around two independent loops started in `Main`:

1. **Render loop.** A `javax.swing.Timer` fires every 16 ms and calls `GamePanel.repaint()`. `GamePanel.paintComponent` passes the `Graphics2D` context and panel dimensions to `RendererDef.render`, which calls `render` on each registered `Renderable` in insertion order. The `Environment` background is currently the only registered renderable. Each `Renderable` exposes a z-index (`Environment` uses −2, placing it at the back), though `RendererDef` does not yet sort by it.

2. **Update loop.** A `while` loop on the main thread repeatedly retrieves every `GlobalUpdate` listener from an `EventListenerList` and calls its `update()` method. `GameObject` and `RigidBody` both implement `GlobalUpdate`.

**Game-object model.** Every `GameObject` owns a `Transform` (position, size and orientation). A `RigidBody` is attached to a `GameObject` and, on each update, adds its velocity to the object's position. Collision handling follows the Java event-listener pattern: `RigidBody.onCollision` creates no events itself, but notifies every registered `CollisionListener` with a `CollisionEvent` whose source is the rigid body.

```
Main ─┬─ Swing Timer (16 ms) ─► GamePanel.repaint() ─► RendererDef.render() ─► Renderable.render()
      └─ Update loop ─► GlobalUpdate.update() ─► RigidBody.update() ─► Transform.position += velocity
```

## Code Reference

Full per-class documentation is available in [DOCUMENTATION.md](DOCUMENTATION.md) once generated. The key types are summarised below.

### Classes

| Class | Purpose | Key members |
|---|---|---|
| `GameWindow` | Application window. | Constructor adds the given panel and sizes the frame to the screen size minus `X_BOUND_OFFSET` (300 px) and `Y_BOUND_OFFSET` (150 px). `show()` makes the frame visible. |
| `GamePanel` | Swing panel that hosts rendering. | `paintComponent(Graphics)` delegates to `RendererDef.render`. |
| `RendererDef` | Ordered collection of drawable objects. | `add(Renderable)`, `remove(Renderable)`, `render(Graphics2D, width, height, x, y)`. Named to avoid a conflict with `javax.swing.Renderer`. |
| `Environment` | Background renderable. | Loads `images/Background.png` in its constructor; `getZindex()` returns −2; `render(...)` draws the image across the panel. |
| `Sprite` | Holds a single image for a game entity. | `getSprite()`, `setSprite(Image)`, `setSpriteByName(String)`. Not yet `Renderable`. |
| `GameObject` | Base class for world objects. | `transform` (`Transform`), `visible` (`boolean`, default `true`); constructors accept a `Transform` or position, size and orientation. |
| `Transform` | Spatial state of a game object. | `position` and `size` (`Vector3`), `orientation` (`double`). Default: position (0, 0, 0), size (1, 1, 1), orientation 0. |
| `Vector3` | Three-component vector. | Fields `x`, `y`, `z`; `sum()` and `magnitude()`. |
| `RigidBody` | Physics component for a game object. | Private `velocity`, `centerOfMass`, `mass`; `update()` applies velocity to position; `addCollisionListener` / `removeCollisionListener`; `onCollision(CollisionEvent)` notifies listeners. |
| `CollisionEvent` | Event raised on collision (declared in `RigidBody.java`). | Source is the `RigidBody` involved. |
| `Vehicle` | Abstract base class for vehicles. | Private `isCrashed` flag (used for towing); stub methods `centerOnLane(int)` and `changeState()`. |
| `VehicleState` | Vehicle states. | `Driving`, `Panic`, `Damaged`, `Breakdown`, `Destroyed`, with intended transitions Driving → Panic/Damaged, Panic → Driving/Damaged, Damaged → Breakdown/Destroyed. |
| `Cars` | Registry of car statistics. | Static `getCarStats(String)` and `populateCarStats()`; currently registers one placeholder entry, `"Temp"`. |
| `CarStats` | Immutable record of a car's statistics. | `frameDurability` (`int`), `hitbox` (`Hitbox`). |
| `Timing` | Static frame-timing access. | `deltaTime()` and `fixedDeltaTime()`; values not yet calculated. |
| `Hitbox` | Placeholder for collision bounds. | No members yet. |

### Interfaces

| Interface | Purpose |
|---|---|
| `Renderable` | Implemented by drawable objects: `getZindex()` and `render(Graphics2D, width, height, x, y)`. |
| `GlobalUpdate` | Implemented by objects updated each loop iteration; provides a default no-op `update()`. |
| `CollisionListener` | Receives `collided(CollisionEvent)` notifications from a `RigidBody`. |

### Global variables and constants

| Name | Location | Value | Purpose |
|---|---|---|---|
| `isRunning` | `Main` | `true` | Controls the update loop; the loop runs while this is `true`. |
| `updateListeners` | `Main` | empty `EventListenerList` | Registry of `GlobalUpdate` listeners called by the update loop. |
| Repaint interval | `Main` | 16 ms | Swing timer delay between repaints (about 60 FPS). |
| `X_BOUND_OFFSET` | `GameWindow` | 300 | Pixels subtracted from screen width when sizing the window. |
| `Y_BOUND_OFFSET` | `GameWindow` | 150 | Pixels subtracted from screen height when sizing the window. |
| `carStatsMap` | `Cars` | `HashMap<String, CarStats>` | Car statistics keyed by car name. |

## Team and Contributions

<!-- TODO: replace GitHub usernames with names and confirm roles -->

| Name | GitHub | Role | Areas of responsibility (from commit history) |
|---|---|---|---|
| <!-- Ryan _______ --> | Vistalix | <!-- Backend --> | `RigidBody`, `GlobalUpdate`, `Vehicle`, consolidation of vector types into `Vector3` |
| <!-- _____________ --> | beenc | <!-- TODO --> | Initial project setup, main update loop in `Main`, collision handling |
| <!-- _____________ --> | DerplordX | <!-- TODO --> | `Sprite`, mass and centre of mass in `RigidBody` |
| <!-- Brody _______ --> | BigBoyBrody | <!-- Frontend --> | Rendering pipeline (`RendererDef`, `Renderable`, `Environment`, `GamePanel`, `GameWindow`) |
| <!-- Brayden Nadeau --> | Brandeau | <!-- Frontend --> | Documentation and Rendering (`RendererDef`, `Renderable`, `Environment`, `GamePanel`, `GameWindow`) |
