package models;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A data model representing a volleyball team
 * Encapsulates the team's identity and performance statistics
 */
public class Team {
    // Immutable attributes
    // Marked 'final' to guarantee the tree's structural integrity is not corrupted by an ID change
    private final int uniqueID;
    private final String name;

    // Mutable attributes
    // Tracks the performance of the team
    private int wins;
    private int losses;

    // Constructor for a Team object with an initial record
    public Team(int uniqueID, int wins, int losses, String name) {
        this.uniqueID = uniqueID;
        this.wins = wins;
        this.losses = losses;
        this.name = name;
    }

    // GETTERS
    public int getID() { return uniqueID; }

    public String getName() { return name; }

    public int getWins() { return wins; }

    public int getLosses() { return losses; }

    // BEHAVIORS

    // Increments the win count by 1
    public void addWin() { wins++; }

    // Increments the loss count by 1
    public void addLoss() { losses++; }

    /**
     * Calculates the team's win percentage
     * Utilized by the Quick Sort algorithm to rank the leaderboard
     * * @return the win percentage is rounded to 2 decimal places (e.g. 0.75)
     */
    public double getWinPercentage() {
        int totalGames = wins + losses;

        // Prevents division by zero if team has played no game
        if (totalGames == 0) { return 0.00; }

        // Cast to double to prevent truncation
        double winPercentage = (double) wins / totalGames;

        // Utilize BigDecimal for rounding to 2 decimal places
        return BigDecimal.valueOf(winPercentage).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }


}
