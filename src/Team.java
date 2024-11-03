import java.util.*;

public class Team extends Squad implements Comparator<Player> {

    private int points = 0;

    Team(String teamName, Manager manager) {
        super(teamName, manager);
        this.points = 0;
    }

    // getter and setter for points (group stage):

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    // should return the best players on the team
    public ArrayList<Player> getMyPlayers(ArrayList<Player> wholeSquad, String formation) {
        // ArrayList to store the players for different Positions:
        ArrayList<Player> GoalKeepers = new ArrayList<>();
        ArrayList<Player> Forwards = new ArrayList<>();
        ArrayList<Player> Midfielders = new ArrayList<>();
        ArrayList<Player> Defenders = new ArrayList<>();

        // sort the players into categories (Forwards, Midfielders, Defenders,
        // Goalkeeper)
        for (Player player : wholeSquad) {
            // Menu system:
            if ((player.getPosition().strip()).equals("Goal Keeper")) {
                GoalKeepers.add(player);
            } else if ((player.getPosition().strip()).equals("Forward")) {
                Forwards.add(player);
            } else if ((player.getPosition().strip()).equals("Midfielder")) {
                Midfielders.add(player);
            } else {
                Defenders.add(player);
            }
        }
        // compare all the Players in each category and sort them:
        ArrayList<Player> sortedGoalKeepers = compareGK(GoalKeepers);
        ArrayList<Player> sortedForwards = compareFW(Forwards);
        ArrayList<Player> sortedMidfielders = compareMF(Midfielders);
        ArrayList<Player> sortedDefenders = compareDF(Defenders);

        // array with the final team
        ArrayList<Player> startingTeam = new ArrayList<>();
        startingTeam.add(sortedGoalKeepers.get(0));// adds the goalkeeper

        // split the formation:
        String[] splitFormation = formation.split("-");
        String numberOfDefenders = splitFormation[0];
        String numberOfMidfielders = splitFormation[1];
        String numberOfForwards = splitFormation[2];

        // pushes the array of the top players retrieved onto the starting team array:
        for (Player top : getThePlayers(sortedForwards, numberOfForwards)) {
            startingTeam.add(top);
        }
        // repeat for midfielders
        for (Player top : getThePlayers(sortedMidfielders, numberOfMidfielders)) {
            startingTeam.add(top);
        }
        // repeat for defenders:
        for (Player top : getThePlayers(sortedDefenders, numberOfDefenders)) {
            startingTeam.add(top);
        }

        return startingTeam;
    }

    // Retrieve the players for the formation:
    public ArrayList<Player> getThePlayers(ArrayList<Player> array, String number) {
        int numb = Integer.parseInt(number);
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < numb; i++) {
            players.add(array.get(i));
        }
        return players;

    }

    // compare defenders
    private ArrayList<Player> compareDF(ArrayList<Player> defenders) {
        return comparePlayers(defenders);
    }

    // compare midfielders
    private ArrayList<Player> compareMF(ArrayList<Player> midfielders) {
        return comparePlayers(midfielders);
    }

    // compare forwards
    private ArrayList<Player> compareFW(ArrayList<Player> forwards) {
        return comparePlayers(forwards);
    }

    // compare goal keepers
    public ArrayList<Player> compareGK(ArrayList<Player> goalKeepers) {
        // return now sorted arrayList
        return comparePlayers(goalKeepers);
    }

    // function to compare the values in the array
    public ArrayList<Player> comparePlayers(ArrayList<Player> array) {
        // method inside the Player class to retrieve the Player's average:
        // comparator interface:
        Collections.sort(array, new Comparator<Player>() {
            @Override
            // This compare method returns an Array in Descending order
            public int compare(Player o1, Player o2) {
                if (o1.getPlayersAverage() == o2.getPlayersAverage()) {
                    return 0;
                } else if (o1.getPlayersAverage() > o2.getPlayersAverage()) {
                    return -1;
                }
                return 1;
            }
        });
        // return now sorted arrayList
        return array;
    }

    // required for the program to run:
    @Override
    public int compare(Player o1, Player o2) {
        if (o1.getPlayersAverage() == o2.getPlayersAverage()) {
            return 0;
        } else if (o1.getPlayersAverage() > o2.getPlayersAverage()) {
            return 1;
        }
        return -1;
    }

    // fitness : ALL
    // passingAccuracy : Midfielders, Defenders
    // shotAccuracy : Forwards combine the 2 and calculate the average
    // shotFrequency : Forwards combine the 2 and calculate the average
    // defensiveness : Defenders
    // aggression : Not Considered
    // positioning : Forwards, Midfielders
    // dribbling : All but Goalkeeper
    // chanceCreation : Midfielders
    // offsideAdherence : How many times a person is offside
}