package ui;
import engine.TournamentManager;
import models.Team;
import models.Match;

import java.util.Scanner;

/**
 * The user interface of the application
 * Gathers input via the Scanner
 * and route it to the TournamentManager engine
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Instantiate the engine and set max match capacity to 100
        TournamentManager engine = new TournamentManager(100);
        boolean running = true;

        System.out.println("Welcome to the Volleyball Tournament Manager!");

        // Loop
        // Returns to main menu after every action until user exits
        while (running) {
            System.out.println("\n---MAIN MENU---");
            System.out.println("1. Register a team");
            System.out.println("2. View Global Leaderboard");
            System.out.println("3. Find Match by Time");
            System.out.println("4. Schedule a new Match");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();     // Clears the newline character

            // Routes the user's choice to the corresponding algorithm
            switch(choice) {

                // Case 1: Gather Team Data and Insert into AVL Tree
                case 1:
                    System.out.println("Enter Team Name");
                    String name = scanner.nextLine();

                    System.out.println("Enter TeamID");
                    int uid = scanner.nextInt();

                    System.out.println("Enter Wins");
                    int wins = scanner.nextInt();

                    System.out.println("Enter Losses");
                    int losses = scanner.nextInt();
                    scanner.nextLine(); // Clears the newline

                    // Instantiate data model and pass it into engine
                    Team newTeam = new Team(uid, wins, losses, name);
                    engine.registerTeam(newTeam);
                    System.out.println("All set! " + name + " has been added to the database!");
                    break;

                // Case 2: Trigger Quick Sort and Print Leaderboard
                case 2:
                    System.out.println("\n --- GLOBAL LEADERBOARD ---");

                    // Engine handles the O(N log N) extraction and sorting
                    Team[] leaderboard = engine.getLeaderboard();

                    if(leaderboard.length == 0) {
                        System.out.println("No teams registered yet.");
                    } else {
                        // Print formatted results
                        for (int i = 0; i < leaderboard.length; i++) {
                            Team team = leaderboard[i];
                            System.out.println((i+1) + ". " + team.getName() +
                                    " | Wins: " + team.getWins() +
                                    " | Win %: " + team.getWinPercentage());
                        }
                    }
                    break;

                // Case 3: Parse Time and Trigger Binary Search
                case 3:
                    System.out.println("Enter time (e.g. 2:30 or 10:00):");
                    String userTime = scanner.nextLine();

                    System.out.println("AM or PM?");
                    String amPm = scanner.nextLine();

                    int convertedTime = convertToMilitaryTime(userTime, amPm);

                    // Trigger the O(log N) Binary Search
                    Match foundMatch = engine.findMatchByTime(convertedTime);

                    if (foundMatch == null) {
                        System.out.println("No match scheduled at this time");
                    } else {
                        System.out.println("\n --- MATCH FOUND ---");
                        System.out.println("Time: " + userTime + " " + amPm.toUpperCase());
                        System.out.println("Court: " + foundMatch.getCourtNumber());
                        System.out.println("Teams playing: ID " + foundMatch.getTeamAId() + " vs ID " + foundMatch.getTeamBId());
                    }

                    break;

                case 4:
                    System.out.println("Enter match time (e.g. 2:30 or 10:00):");
                    String matchTimeStart = scanner.nextLine();

                    System.out.println("AM or PM?");
                    String matchAmPm = scanner.nextLine();

                    int scheduledTime = convertToMilitaryTime(matchTimeStart, matchAmPm);

                    System.out.println("Enter Court Number");
                    int court = scanner.nextInt();

                    System.out.println("Enter Team A's ID");
                    int teamA = scanner.nextInt();

                    System.out.println("Enter Team B's ID");
                    int teamB = scanner.nextInt();
                    scanner.nextLine(); // Clear the newline

                    Match newMatch = new Match(scheduledTime, court, teamA, teamB, false);

                    engine.scheduleMatch(newMatch);
                    System.out.println("Success! Match scheduled at " + matchTimeStart + " "
                            + matchAmPm.toUpperCase() + " on Court " + court);
                    break;

                // Case 5: Shut Down
                case 5:
                    System.out.println("Shutting down. Goodbye!");
                    running = false; // Breaks the while loop
                    break;

                default:
                    System.out.println("Invalid Choice. Please select a number 1-5.");
            }
        }

        scanner.close();
    }

    /**
     * Helper method to convert 12-hr time format into military integer format
     */
    private static int convertToMilitaryTime(String userTime, String amPm) {
        // Split the string at the colon to separate hours and minutes
        String[] timeParts = userTime.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        // Adjust for 24-hour military time
        if (amPm.equalsIgnoreCase("pm") && hours != 12) {
            hours += 12;
        } else if (amPm.equalsIgnoreCase("am") && hours == 12) {
            hours = 0;
        }

        // Convert to integer format (e.g., 14 * 100 + 30 = 1430)
        return (hours * 100) + minutes;
    }
}
