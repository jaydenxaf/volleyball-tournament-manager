# Dynamic Volleyball Tournament Manager 🏐

A terminal-based Java application for tracking team standings and match schedules. The focus is algorithm efficiency: the core data structures and sorting/searching algorithms are implemented from scratch rather than using `java.util` collections.

## Features
* **Team registry:** Register teams with a unique ID and starting record. Duplicate IDs are rejected.
* **Leaderboard:** Ranks teams by win percentage.
* **Match scheduling:** Inserts matches into a chronological schedule. Rejects unknown team IDs, a team playing itself, and two matches at the same start time.
* **Match lookup:** Finds the match at a given time with binary search.
* **Results:** Recording a winner updates both teams' records, so the leaderboard changes.
* **Input validation:** Menu choices, IDs, records and times are validated and re-prompted instead of crashing.

## Data Structures & Algorithms
* **AVL tree (team database):** `Team` objects are stored in a self-balancing AVL tree keyed by team ID, with rotations and height tracking. Insert and search are O(log N).
* **Quick sort (leaderboard):** The tree is copied to an array in ID order and sorted by win percentage, highest first. Average O(N log N). It uses a middle-element pivot, so already-sorted input and many tied records do not trigger the O(N²) worst case of a last-element pivot.
* **Binary search (match lookup):** Recursive search over the schedule array by military time (e.g. 2:30 PM = 1430), O(log N).
* **Insertion sort (scheduling):** Each new match is shifted into its chronological position. O(N) per insert, which is fine for a schedule capped at 100 matches.

## Architecture
The code is split into four packages:
1. **`ui`:** `Main`, which handles terminal input/output and validation.
2. **`engine`:** `TournamentManager`, which holds the sorting, searching and scheduling logic.
3. **`structures`:** `AVLTree` and `AVLNode`.
4. **`models`:** `Team` and `Match`.

## How to Run
Requires a JDK (Java 8 or newer).

```bash
git clone https://github.com/jaydenxaf/volleyball-tournament-manager.git
cd volleyball-tournament-manager
javac -d out -sourcepath src src/ui/Main.java
java -cp out ui.Main
```

Times are entered as `H:MM` with the hour from 1 to 12, then AM or PM (for example `2:30`, then `PM`).

## Known Limitations
* The schedule holds at most 100 matches and one match per start time. There is no separate handling of multiple courts at the same time.
* Data is in memory only and is lost when the program exits.
* Team IDs must be whole numbers, and there is no editing or deleting of teams or matches.

---
*Developed by Jayden Flores for CS 254.*
