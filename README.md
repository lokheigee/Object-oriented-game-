Project Overview:

"Shadow Dungeon" is a feature-rich, 2D top-down adventure game developed as a comprehensive exercise in Object-Oriented Software Development. The project emphasizes robust software engineering principles, including modular design, inheritance, polymorphism, and state management, all implemented within a custom game engine framework.

The player, trapped in a mysterious dungeon, must navigate through a series of interconnected rooms, combat enemies, solve environmental puzzles, and manage resources to ultimately succeed. The game features a complete loop from character selection and preparation to combat, exploration, and a conclusive end state.

Key Features & Technical Implementation:

Modular Room System: The game world is structured as a finite state machine of distinct rooms (Preparation, Battle, and End rooms), each with unique properties, entities, and behaviors. A sophisticated door management system controls transitions, locking mechanics, and player spawning between rooms.

Dual-Character System: Players can choose between two unique characters, the Robot or the Marine, each with a specialized passive skill. The Robot gains bonus coins for enemy eliminations, while the Marine is immune to environmental damage from rivers. This design showcases the use of polymorphism and strategy patterns for character abilities.

Dynamic Combat & Entity Management: The game features a variety of enemies with distinct AI behaviors:

Bullet Kin & Ashen Bullet Kin: Stationary enemies that fire projectiles at the player at defined intervals.

Key Bullet Kin: Mobile enemies that patrol predefined paths and drop essential keys upon defeat.
A full physics and collision system governs interactions between players, enemies, player-fired bullets, enemy-fired fireballs, and environmental objects.

Interactive Environment: The dungeon is populated with interactive entities that affect gameplay:

Obstacles: Walls, tables, and baskets that block player movement and can be destroyed.

Hazards: Rivers that continuously damage the player (unless playing as the Marine).

Collectibles: Keys and coins that are essential for progression and purchasing upgrades.

Treasure Boxes: Locked containers that require keys to open, granting coin rewards.

In-Game Economy & Store: A real-time economy allows players to collect coins and spend them in a pause-menu store. Players can purchase health restoration and permanent weapon upgrades, requiring resource management and strategic decision-making.

Data-Driven Design: The game's configuration is entirely externalized through app.properties and message.properties files. This includes entity coordinates, game balance variables (health, damage, speeds), UI messaging, and font specifications. This demonstrates a professional approach to game development, allowing for easy tuning and localization without code changes.

Polished User Experience: The project includes a complete UI/UX flow with a start screen, real-time HUD displaying player stats (health, coins, keys, weapon level), a pause menu, and end-game screens, all rendered with custom sprites and typography.

Technical Highlights:

Architecture: Built upon a clear Object-Oriented Architecture, leveraging inheritance hierarchies for entities (e.g., Enemy -> BulletKin, Player -> Marine) and composition for systems like collision and rendering.

State Management: Implements complex state logic for room progression, door locking/unlocking, character selection, and the game's pause/store system.

Collision & Event Handling: Manages a wide array of collision interactions and real-time input from both keyboard and mouse for movement, aiming, and shooting
