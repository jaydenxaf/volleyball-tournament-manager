package engine;

import structures.AVLTree;
import models.Match;
import models.Team;

/**
 * Core engine of the application
 * Manages the AVL database of teams and the array of Match objects,
 * executing the Quick Sort and Binary Search algorithms
 */
public class TournamentManager {

    // The self-balancing database holding all Team objects
    private AVLTree tree;

    // The chronological schedule of all matches
    private Match[] matches;

    // Tracks the number of scheduled matches
    private int matchCount;

    // Constructor -- Initializes a new database and an empty schedule array
    public TournamentManager(int maxMatches) {
        this.tree = new AVLTree();
        this.matches = new Match[maxMatches];
        this.matchCount = 0;
    }

    // Takes a team object and inserts it into the AVL Tree
    // Returns false if the ID is already taken
    public boolean registerTeam(Team t) {
        return tree.insert(t);
    }

    // Looks up a team by ID in O(log N); returns null if it does not exist
    public Team findTeam(int id) {
        return tree.search(id);
    }

    /**
     * Records a finished match: gives the winner a win and the loser a loss,
     * which changes the leaderboard. Returns false if either ID is unknown,
     * the IDs are the same, or the match was not found/already finished.
     */
    public boolean recordResult(Match match, int winnerId) {
        if (match == null || match.isMatchFinished()) return false;
        if (winnerId != match.getTeamAId() && winnerId != match.getTeamBId()) return false;
        int loserId = (winnerId == match.getTeamAId()) ? match.getTeamBId() : match.getTeamAId();
        Team winner = tree.search(winnerId);
        Team loser = tree.search(loserId);
        if (winner == null || loser == null) return false;
        winner.addWin();
        loser.addLoss();
        match.finishMatch();
        return true;
    }

    /**
     * Extracts all teams from the tree into an array
     * Executes quick sort to rank them by win percentage
     * Returns the sorted leaderboard in O(N log N) average time
     */
    public Team[] getLeaderboard() {
        Team[] allTeams = tree.toArray();

        // Sorts array using the recursive Quick Sort
        quickSort(allTeams, 0, allTeams.length - 1);

         return allTeams;
    }

    // A recursive divide-and-conquer algorithm that sorts the team array
    // Average O(N log N). The pivot is the middle element (moved to the end), so
    // already-sorted input and long runs of tied win percentages avoid the O(N^2)
    // worst case that a fixed last-element pivot would hit
    public static void quickSort(Team[] allTeams, int low, int high) {
        if (low < high) {
            // Finds the correct spot for the pivot
            int pi = quickSortPartition(allTeams, low, high);

            // Recursively sort the left and right halves
            quickSort(allTeams, low, pi - 1);
            quickSort(allTeams, pi + 1, high);
        }
    }

    /**
     * Compares the win percentages of the teams
     * Pushes the highest percentages to the front
     */
    private static int quickSortPartition(Team[] allTeams, int low, int high) {
        // Move the middle element to the end so it acts as the pivot
        swap(allTeams, low + (high - low) / 2, high);
        Team pivot = allTeams[high];
        int i = low - 1;

        for (int j = low; j <= high - 1; j++) {
            // ">" results in descending order (highest win % first)
            if (allTeams[j].getWinPercentage() > pivot.getWinPercentage()) {
                i++;
                swap(allTeams, i, j);
            }
        }

        // Place pivot in its correct position
        swap(allTeams, i+1, high);
        return i + 1;
    }

    // Helper method to swap two Team objects in the array
    private static void swap(Team[] arr, int i, int j) {
        Team temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Public Wrapper method to begin the Binary Search takes a military time,
     * searches the match array, and returns the match happening at that time in
     * O(log N) time
     */

    public Match findMatchByTime(int time) {
        // 'matchCount - 1' avoids searching null slots
        int index = binarySearch(matches, 0, matchCount - 1, time);

        // Checks if search failed to find the time
        if (index == -1) return null;

        return matches[index];
    }


    /**
     * Private recursive engine for the Binary Search algorithm
     * Divides the array in half repeatedly until the military time key is found
     */
    private int binarySearch(Match[] matches, int l, int r, int x) {

        if (r >= l) {
            // Calculates midpoint
            int mid = l + (r - l) / 2;

            // Match time was found - Returns index of match
            if (matches[mid].getTime() == x)
                return mid;

            // Search left subarray: Target time is earlier than the middle match
            if (matches[mid].getTime() > x)
                return binarySearch(matches, l, mid - 1, x);

            // Search right subarray: Target time is later than the middle match
            return binarySearch(matches, mid + 1, r, x);
        }

        // Base Case: Pointers crossed and time does not exist
        return -1;
    }

    /**
     * Adds a newly scheduled match to the array
     * Uses Insertion Sort to guarantee the array remains chronologically sorted
     */
    // Returns null on success, or a short error message explaining why it was rejected
    public String scheduleMatch(Match newMatch) {
        if (matchCount >= matches.length) {
            return "The schedule is completely full.";
        }
        if (newMatch.getTeamAId() == newMatch.getTeamBId()) {
            return "A team cannot play itself.";
        }
        if (tree.search(newMatch.getTeamAId()) == null) {
            return "Team ID " + newMatch.getTeamAId() + " is not registered.";
        }
        if (tree.search(newMatch.getTeamBId()) == null) {
            return "Team ID " + newMatch.getTeamBId() + " is not registered.";
        }
        // Only one match per start time, so a binary search by time is unambiguous
        if (findMatchByTime(newMatch.getTime()) != null) {
            return "A match is already scheduled at that time.";
        }

        // Start at end of array
        int i = matchCount - 1;

        // Slide existing matches to the right if
        while (i >= 0 && matches[i].getTime() > newMatch.getTime()) {
            matches[i+1] = matches[i];
            i--;
        }

        // Drop the new match into the correct chronological spot
        matches[i + 1] = newMatch;
        matchCount++;
        return null;
    }

}
