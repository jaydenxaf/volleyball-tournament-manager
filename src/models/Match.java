package models;

/**
 * Acts as a data container for schedule and tracking individual matches
 */

public class Match {
    // Immutable attributes
    // Marked final to prevent accidental overwrites
    private final int courtNumber;
    private final int teamAId;
    private final int teamBId;

    // Mutable attributes
    // Stored as an integer for the Binary Search
    private int time;
    // Tracks the live state of the match
    private boolean matchFinished;

    // Constructor to schedule a new match and set its initial state
    public Match(int time, int courtNumber, int teamAId, int teamBId, boolean matchFinished) {
        this.time = time;
        this.courtNumber = courtNumber;
        this.teamAId = teamAId;
        this.teamBId = teamBId;
        this.matchFinished = matchFinished;
    }

    // GETTERS

    // Return the military time of the match
    // Used by the Binary Search algorithm
    public int getTime() { return time; }

    public int getCourtNumber() { return courtNumber; }

    // Returns the unique ID of team A -- used to query AVL tree and fetch the team object
    public int getTeamAId() { return teamAId; }

    // Returns the unique ID of team B -- used to query AVL tree and fetch the team object
    public int getTeamBId() { return teamBId; }

    public boolean isMatchFinished() { return matchFinished; }

    // SETTERS

    // Updates the state of the match
    public void finishMatch() {
        matchFinished = true;
    }
}
