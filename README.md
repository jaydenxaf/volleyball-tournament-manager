# Dynamic Volleyball Tournament Manager 🏐

A Java-based backend tournament management system designed to handle the real-time logistical challenges of tracking team standings and match schedules. Built as a terminal-based CRUD application, this project focuses on optimal memory management and algorithm efficiency by implementing self-balancing data structures from scratch.

## Features
* **Live Leaderboards:** Dynamically ranks teams based on win/loss percentages.
* **Match Scheduling:** Inserts new matches into a strict chronological timeline.
* **Rapid Retrieval:** Instantly searches for matches by time without scanning the entire database.
* **Crash-Proof UI:** Terminal interface handles invalid user inputs safely to prevent fatal runtime exceptions.

## Data Structures & Algorithms
To ensure optimal performance as the tournament scales, this application avoids sluggish standard arrays and instead relies on advanced, custom-built data structures:

* **AVL Tree (Primary Database):** All `Team` objects are stored in a self-balancing AVL Tree using their unique ID as the key. This guarantees **O(log N)** time complexity for all team insertions and searches.
* **Quick Sort:** When generating the leaderboard, the system extracts the AVL Tree to an array and utilizes a Quick Sort algorithm to rank teams by win percentage in **O(N log N)** time.
* **Binary Search:** The scheduling engine utilizes a recursive Binary Search to locate scheduled matches in **O(log N)** time.
* **Insertion Sort:** When scheduling a new match, the engine uses an Insertion Sort to safely shift existing matches to the right, maintaining a strict chronological array without throwing `ArrayIndexOutOfBoundsExceptions`.

## Software Architecture
To strictly adhere to the **Single Responsibility Principle**, the codebase is modularized into a 4-layer architecture:
1. **`ui` (User Interface):** Houses the `Main` class and scanner logic for terminal interactions.
2. **`engine` (Business Logic):** Houses the `TournamentManager`, isolating all sorting and searching algorithms.
3. **`structures` (Data Management):** Contains the custom `AVLTree` and `AVLNode` classes.
4. **`models` (Data Containers):** Contains the encapsulated `Team` and `Match` objects.

## How to Run
Ensure you have the Java Development Kit (JDK) installed on your machine.
1. Clone the repository: `git clone https://github.com/jaydenxaf/volleyball-tournament-manager.git`
2. Navigate to the project directory.
3. Compile the Java files: `javac src/ui/Main.java` (Adjust path depending on IDE setup).
4. Run the application: `java src.ui.Main`

---
*Developed by Jayden Flores for CS-253.*
