package ui;
import engine.TournamentManager;
import models.Team;
import models.Match;

import java.util.Scanner;

/**
 * The user interface of the application
 * Gathers input via the Scanner (validating every entry)
 * and routes it to the TournamentManager engine
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Instantiate the engine and set max match capacity to 100
        TournamentManager engine = new TournamentManager(100);
        boolean running = true;

        System.out.println("Welcome to the Volleyball Tournament Manager!");

        // Returns to main menu after every action until user exits
        while (running) {
            System.out.println("\n---MAIN MENU---");
            System.out.println("1. Register a team");
            System.out.println("2. View Global Leaderboard");
            System.out.println("3. Find Match by Time");
            System.out.println("4. Schedule a new Match");
            System.out.println("5. Record a match result");
            System.out.println("6. Exit");

            // readInt returns null only when input has ended (e.g. Ctrl+D)
            Integer choice = readInt("Select an option: ", 1, 6);
            if (choice == null) break;

            switch (choice) {

                // Case 1: Gather Team Data and Insert into AVL Tree
                case 1: {
                    String name = readLine("Enter Team Name: ");
                    if (name == null) { running = false; break; }
                    if (name.isEmpty()) {
                        System.out.println("Team name cannot be empty.");
                        break;
                    }
                    Integer uid = readInt("Enter TeamID: ", 0, Integer.MAX_VALUE);
                    Integer wins = readInt("Enter Wins: ", 0, 10000);
                    Integer losses = readInt("Enter Losses: ", 0, 10000);
                    if (uid == null || wins == null || losses == null) { running = false; break; }

                    if (engine.registerTeam(new Team(uid, wins, losses, name))) {
                        System.out.println("All set! " + name + " has been added to the database!");
                    } else {
                        System.out.println("Team ID " + uid + " already exists. Nothing was added.");
                    }
                    break;
                }

                // Case 2: Trigger Quick Sort and Print Leaderboard
                case 2: {
                    System.out.println("\n --- GLOBAL LEADERBOARD ---");
                    Team[] leaderboard = engine.getLeaderboard();

                    if (leaderboard.length == 0) {
                        System.out.println("No teams registered yet.");
                    } else {
                        for (int i = 0; i < leaderboard.length; i++) {
                            Team team = leaderboard[i];
                            System.out.println((i + 1) + ". " + team.getName() +
                                    " (ID " + team.getID() + ")" +
                                    " | Wins: " + team.getWins() +
                                    " | Losses: " + team.getLosses() +
                                    " | Win %: " + team.getWinPercentage());
                        }
                    }
                    break;
                }

                // Case 3: Parse Time and Trigger Binary Search
                case 3: {
                    Integer time = readTime();
                    if (time == null) { running = false; break; }

                    Match foundMatch = engine.findMatchByTime(time);
                    if (foundMatch == null) {
                        System.out.println("No match scheduled at this time");
                    } else {
                        printMatch(foundMatch, time);
                    }
                    break;
                }

                // Case 4: Schedule a match
                case 4: {
                    Integer time = readTime();
                    if (time == null) { running = false; break; }
                    Integer court = readInt("Enter Court Number: ", 1, 999);
                    Integer teamA = readInt("Enter Team A's ID: ", 0, Integer.MAX_VALUE);
                    Integer teamB = readInt("Enter Team B's ID: ", 0, Integer.MAX_VALUE);
                    if (court == null || teamA == null || teamB == null) { running = false; break; }

                    String error = engine.scheduleMatch(new Match(time, court, teamA, teamB, false));
                    if (error == null) {
                        System.out.println("Success! Match scheduled at " + formatTime(time)
                                + " on Court " + court);
                    } else {
                        System.out.println("Could not schedule match: " + error);
                    }
                    break;
                }

                // Case 5: Record a result (updates the leaderboard)
                case 5: {
                    Integer time = readTime();
                    if (time == null) { running = false; break; }
                    Match match = engine.findMatchByTime(time);
                    if (match == null) {
                        System.out.println("No match scheduled at this time");
                        break;
                    }
                    if (match.isMatchFinished()) {
                        System.out.println("That match already has a recorded result.");
                        break;
                    }
                    printMatch(match, time);
                    Integer winner = readInt("Enter the winning team's ID: ", 0, Integer.MAX_VALUE);
                    if (winner == null) { running = false; break; }
                    if (engine.recordResult(match, winner)) {
                        System.out.println("Result recorded. The leaderboard has been updated.");
                    } else {
                        System.out.println("That ID is not one of the two teams in this match.");
                    }
                    break;
                }

                // Case 6: Shut Down
                case 6:
                    System.out.println("Shutting down. Goodbye!");
                    running = false;
                    break;
            }
        }

        scanner.close();
    }

    // ---------- Input helpers ----------

    // Prints a prompt and returns the trimmed line, or null if input has ended
    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) return null;
        return scanner.nextLine().trim();
    }

    // Keeps asking until the user enters a whole number in [min, max]; null if input ended
    private static Integer readInt(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt);
            if (line == null) return null;
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException e) {
                // fall through to the message below
            }
            System.out.println("Please enter a whole number from " + min + " to "
                    + (max == Integer.MAX_VALUE ? "2147483647" : String.valueOf(max)) + ".");
        }
    }

    // Asks for a time like 2:30 plus AM/PM and returns it as military time (e.g. 1430)
    private static Integer readTime() {
        while (true) {
            String userTime = readLine("Enter time (e.g. 2:30 or 10:00): ");
            if (userTime == null) return null;
            String amPm = readLine("AM or PM? ");
            if (amPm == null) return null;

            int military = convertToMilitaryTime(userTime, amPm);
            if (military >= 0) return military;
            System.out.println("Invalid time. Use H:MM with an hour of 1-12 (e.g. 2:30) and AM or PM.");
        }
    }

    /**
     * Converts 12-hr time to a military integer (e.g. 2:30 PM -> 1430).
     * Returns -1 if the input is not a valid time.
     */
    static int convertToMilitaryTime(String userTime, String amPm) {
        if (!amPm.equalsIgnoreCase("am") && !amPm.equalsIgnoreCase("pm")) return -1;
        String[] parts = userTime.trim().split(":");
        if (parts.length != 2) return -1;

        int hours, minutes;
        try {
            hours = Integer.parseInt(parts[0].trim());
            minutes = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return -1;
        }
        if (hours < 1 || hours > 12 || minutes < 0 || minutes > 59) return -1;

        if (amPm.equalsIgnoreCase("pm") && hours != 12) {
            hours += 12;
        } else if (amPm.equalsIgnoreCase("am") && hours == 12) {
            hours = 0;
        }
        return (hours * 100) + minutes;
    }

    // Turns 1430 back into "2:30 PM"
    private static String formatTime(int military) {
        int h = military / 100;
        int m = military % 100;
        String suffix = h >= 12 ? "PM" : "AM";
        int h12 = h % 12 == 0 ? 12 : h % 12;
        return h12 + ":" + (m < 10 ? "0" + m : String.valueOf(m)) + " " + suffix;
    }

    private static void printMatch(Match m, int time) {
        System.out.println("\n --- MATCH FOUND ---");
        System.out.println("Time: " + formatTime(time));
        System.out.println("Court: " + m.getCourtNumber());
        System.out.println("Teams playing: ID " + m.getTeamAId() + " vs ID " + m.getTeamBId());
        System.out.println("Status: " + (m.isMatchFinished() ? "Finished" : "Scheduled"));
    }
}
