import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static Squad[] squads = new Squad[32];

    public static void main(String[] args) {
        Scanner in = null;// scanner to read the contents of the csv file
        try {
            // Loop through Managers.csv
            File mangers = new File("Managers.csv");
            in = new Scanner(mangers);
            in.nextLine();// ignores the first line in the file
            // loop through each line in the csv file
            while (in.hasNextLine()) {
                // create an array of the manger's properties
                // mP stands for managerProperties
                String[] mP = (in.nextLine()).split("\\,");
                // dC is a function I created to convert a String to a double (just to save some
                // space)
                Manager currentManager = new Manager(mP[0], mP[1], mP[2], mP[3], dC(mP[4]), dC(mP[5]), dC(mP[6]),
                        dC(mP[7]));
                // create the squad (manager and the managers team as the team name)
                Squad tempSquad = new Squad(currentManager.getTeam(), currentManager);

                // get the next available index in the squads array:
                // -1 means the array is full
                int index = insertableIndex();
                if (index > -1) {
                    squads[index] = tempSquad;// place the squad into the squads array
                }
            }

            // Looping through Players.csv
            File players = new File("Players.csv");
            in = new Scanner(players);
            in.nextLine();// ignores the first line in the file
            // loop through each line in the csv file
            while (in.hasNextLine()) {
                // create an array of the player's properties (name etc..)
                // pP stands for playerProperties
                String[] pP = (in.nextLine()).split("\\,");
                // dC is a function I created to convert a String to a double (just to save some
                // space)
                Player currentPlayer = new Player(pP[0], pP[1], pP[2], pP[3], dC(pP[4]), dC(pP[5]), dC(pP[6]),
                        dC(pP[7]), dC(pP[8]), dC(pP[9]), dC(pP[10]), dC(pP[11]), dC(pP[12]), dC(pP[13]));

                // loop through the squads array:
                for (int i = 0; i < squads.length; i++) {
                    String teamName = squads[i].getTeamName();// retrieve the team name
                    // if the team name matches the team name that of the player then add tha player
                    // to the squad
                    if ((currentPlayer.getTeam()).equals(teamName)) {
                        squads[i].addPlayer(currentPlayer);
                    }
                }
            }
        }
        // catch exceptions
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        // closes the file
        finally {
            if (in != null) {
                in.close();
            }
        }
        // Generate the Starting 11 for each Team:
        ArrayList<Team> tournamentSquads = new ArrayList<>();

        // loop through all the squares to pick the best players and add them to the
        // array above
        for (int j = 0; j < squads.length; j++) {
            tournamentSquads.add(getTeam(squads[j]));
        }

        // Test to see all the teams with their starting 11:
        /*
         * for(Team squad: tournamentSquads){
         * System.out.println("Team:"+squad.getTeamName()+" Manager:"+squad.getManager()
         * .getSurname());
         * for(int i = 0; i < 11; i++){
         * System.out.println(squad.getPlayer(i).getSurname()+" "+squad.getPlayer(i).
         * getPosition());
         * }
         * }
         */

        runTournament(tournamentSquads);

    }

    // converts a String to a double (Just Re-named the function to save space)
    public static double dC(String toConvert) {
        return Double.parseDouble(toConvert);
    }

    // get the next available index in the squads array:
    // if array is full it returns -1
    public static int insertableIndex() {
        for (int i = 0; i < squads.length; i++) {
            if (squads[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public static Team getTeam(Squad s) {
        // temp Array of players:
        ArrayList<Player> tempPlayerArray = new ArrayList<>();

        Team t = new Team(s.getTeamName(), s.getManager());

        for (int i = 0; i < squads.length; i++) {
            if ((squads[i].getTeamName()).equals(t.getTeamName())) {
                for (int j = 0; j < 26; j++) {
                    // push all the players into a temporally created array
                    tempPlayerArray.add(squads[i].getPlayer(j));
                }
            }
        }

        ArrayList<Player> startingTeam = t.getMyPlayers(tempPlayerArray, t.getManager().getFavouredFormation());
        for (Player player : startingTeam) {
            t.addPlayer(player);
        }
        return t;
    }

    public static void runTournament(ArrayList<Team> tournamentSquads) {
        // shuffle the teams:
        Collections.shuffle(tournamentSquads);
        // winners of Group Stages
        ArrayList<Team> winnersOfGroupStage = new ArrayList<>();
        // winners of stage1
        ArrayList<Team> winnersOfStage1 = new ArrayList<>();
        // winners of stage2:
        ArrayList<Team> winnersOfStage2 = new ArrayList<>();
        // winners of quarter-final:
        ArrayList<Team> winnersOfQuarter = new ArrayList<>();

        // carry out 12 games for each group: 0: 1,2,3 | 1: 2,3, | 2: 3
        // or another way randomise the teamsNames
        // take the 2 winners from each group
        for (int i = 0; i < tournamentSquads.size(); i++) {
            if ((i + 1) % 4 == 0) {
                // split all the teams into a group
                Team[] returned = groupDivision(tournamentSquads.get(i - 3), tournamentSquads.get(i - 2),
                        tournamentSquads.get(i - 1), tournamentSquads.get(i));
                winnersOfGroupStage.add(returned[0]);
                winnersOfGroupStage.add(returned[1]);
            }
        }
        System.out.println("WINNERS OF GROUP STAGE!");
        for (Team team : winnersOfGroupStage) {
            System.out.println(team.getTeamName());
        }

        // Tournament 1st Stage:
        for (int i = 0; i < 8; i++) {
            // first team plays the last team in the array:
            String result = playMatch(winnersOfGroupStage.get(i), winnersOfGroupStage.get(15 - i), false);
            if (result.equals("t1")) {
                winnersOfStage1.add(winnersOfGroupStage.get(i));
            } else {
                winnersOfStage1.add(winnersOfGroupStage.get(15 - i));
            }
        }
        System.out.println("WINNERS OF STAGE 1!");
        for (Team team : winnersOfStage1) {
            System.out.println(team.getTeamName());
        }

        // Tournament Stage 2:
        for (int i = 0; i < 4; i++) {
            // first team plays the last team in the array:
            String result = playMatch(winnersOfStage1.get(i), winnersOfStage1.get(7 - i), false);
            if (result.equals("t1")) {
                winnersOfStage2.add(winnersOfStage1.get(i));
            } else {
                winnersOfStage2.add(winnersOfStage1.get(7 - i));
            }
        }

        System.out.println("Teams Going Into Quarter Finals!");
        for (Team team : winnersOfStage2) {
            System.out.println(team.getTeamName());
        }

        // Tournament Quarter Finals:
        for (int i = 0; i < 2; i++) {
            String result = playMatch(winnersOfStage2.get(i), winnersOfStage2.get(3 - i), false);
            if (result.equals("t1")) {
                winnersOfQuarter.add(winnersOfStage2.get(i));
            } else {
                winnersOfQuarter.add(winnersOfStage2.get(3 - i));
            }
        }
        System.out.println("Teams facing in the Final!");
        for (Team team : winnersOfQuarter) {
            System.out.println(team.getTeamName());
        }

        String result = playMatch(winnersOfQuarter.get(0), winnersOfQuarter.get(1), false);
        if (result.equals("t1")) {
            System.out.println(winnersOfQuarter.get(0).getTeamName() + " HAS WON THE WORLD CUP!!!");
        } else {
            System.out.println(winnersOfQuarter.get(1).getTeamName() + " HAS WON THE WORLD CUP!!!");
        }

    }

    // divide the teams into groups:
    public static Team[] groupDivision(Team teamA, Team teamB, Team teamC, Team teamD) {
        // arrayList to store the score for each team during the groupStage:
        ArrayList<Team> groupStageTeams = new ArrayList<>();
        System.out.println("Group");
        // adding teams to the Array:
        groupStageTeams.add(0, teamA);
        groupStageTeams.add(1, teamB);
        groupStageTeams.add(2, teamC);
        groupStageTeams.add(3, teamD);

        // possible matches:
        ArrayList<String> combinations = new ArrayList<>();
        combinations.add(0, "0-1");
        combinations.add(0, "0-2");
        combinations.add(0, "0-3");
        combinations.add(0, "1-2");
        combinations.add(0, "1-3");
        combinations.add(0, "2-3");
        // randomise the match order:
        randomiseGames(combinations);

        // play the matches (Group Stage):
        for (String match : combinations) {
            String[] currentMatch = match.split("-");
            Team t1 = groupStageTeams.get(Integer.parseInt(currentMatch[0]));
            Team t2 = groupStageTeams.get(Integer.parseInt(currentMatch[1]));
            // retrieve the result of the match played
            // 'canDraw', is the boolean to check that the match being played
            // is in group stage
            // winner gets 2 points:
            String result = playMatch(t1, t2, true);
            if (result.equals("t1")) {
                int points = t1.getPoints();
                t1.setPoints(points += 2);
            } else if (result.equals("t2")) {
                int points = t2.getPoints();
                t2.setPoints(points += 2);
            }
            // draw then both teams get a point:
            else if (result.equals("draw")) {
                int t1Points = t1.getPoints();
                int t2Points = t2.getPoints();
                t1.setPoints(t1Points += 1);
                t2.setPoints(t2Points += 1);
            }
        }
        // return the winners of the group stage:
        Team[] order = new Team[4];
        order[0] = teamA;
        order[1] = teamB;
        order[2] = teamC;
        order[3] = teamD;

        // sort the array:
        Team[] sortedOrder = sort(order);

        Team[] returnTeam = new Team[2];
        returnTeam[0] = sortedOrder[3];
        returnTeam[1] = sortedOrder[2];
        return returnTeam;
    }

    // Insertion sort to sort the team:
    public static Team[] sort(Team[] arr) {
        // length:
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            Team key = arr[i];// teamB
            int j = i - 1;// new int created
            while (j >= 0 && arr[j].getPoints() > key.getPoints()) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
        return arr;
    }

    // Play match method:
    public static String playMatch(Team t1, Team t2, boolean canDraw) {
        System.out.println("Match:");
        System.out.println(t1.getTeamName() + " VS " + t2.getTeamName());
        // Chance to Score a goal:
        int t1ChancesToScore = (int) chancesToScore(t1);
        int t2ChancesToScore = (int) chancesToScore(t2);

        // Chance to Save a goal:
        int t1ChanceToSave = (int) chancesToSave(t1);
        int t2ChanceToSave = (int) chancesToSave(t2);

        // calculate the chance of goal:
        // if it is in the group stage then there is a chance of Draw
        // So this tournament is not too boring I am going to give each team 15 shots
        int team1Goals = 0;
        int team2Goals = 0;
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            boolean x = random.nextBoolean();
            // if true then its t1's shot on goal
            if (x) {
                if (shotAttempt(t1ChancesToScore, t2ChanceToSave, t1)) {
                    team1Goals += 1;
                }
                ;
            } else {
                if (shotAttempt(t2ChancesToScore, t1ChanceToSave, t2)) {
                    team2Goals += 1;
                }

            }
        }
        // match result:
        System.out.println(
                "Match Result: " + t1.getTeamName() + " " + team1Goals + "   " + team2Goals + " " + t2.getTeamName());
        // GroupStage:
        if (canDraw) {
            if (team1Goals > team2Goals) {
                return "t1";
            } else if (team1Goals < team2Goals) {
                return "t2";
            } else {
                return "draw";
            }
        }
        // After Group Stage:
        else {
            if (team1Goals > team2Goals) {
                return "t1";
            } else if (team1Goals < team2Goals) {
                return "t2";
            } else {
                // penalties:
                System.out.println("Match has gone to Penalty shoot-out!");
                // normal penalty stage:
                // who shoots the penalty
                Player[] t1Players = penaltyOrder(t1);
                Player[] t2Players = penaltyOrder(t2);
                int t1PenaltiesMade = 0;
                int t2PenaltiesMade = 0;
                // goalie is in pos 10
                for (int i = 0; i < 5; i++) {
                    // first 5 players:
                    int result = penaltyShot(t1Players[(t1Players.length - 1) - i], t2Players[10]);
                    t1PenaltiesMade += result;
                    int result2 = penaltyShot(t2Players[(t1Players.length - 1) - i], t2Players[10]);
                    t2PenaltiesMade += result2;
                }
                System.out.println("Penalty result: " + t1.getTeamName() + " " + t1PenaltiesMade + "   "
                        + t2.getTeamName() + " " + t2PenaltiesMade);
                if (t1PenaltiesMade > t2PenaltiesMade) {
                    return "t1";
                } else if (t1PenaltiesMade < t2PenaltiesMade) {
                    return "t2";
                }
                // sudden death penalty:
                else {
                    while (true) {
                        for (int i = 0; i < 10; i++) {
                            int result = penaltyShot(t1Players[(t1Players.length - 1) - i], t2Players[10]);
                            int result2 = penaltyShot(t2Players[(t1Players.length - 1) - i], t1Players[10]);
                            if (result > result2) {
                                System.out.println("Due to Sudden Death " + t1.getTeamName() + " Wins!");
                                return "t1";
                            } else if (result < result2) {
                                System.out.println("Due to Sudden Death " + t2.getTeamName() + " Wins!");
                                return "t2";
                            } else {
                                // goalie v goalie
                                result = penaltyShot(t1Players[10], t2Players[10]);
                                result2 = penaltyShot(t2Players[10], t1Players[10]);
                                if (result > result2) {
                                    System.out.println("Due to Sudden Death " + t1.getTeamName() + " Wins!");
                                    return "t1";
                                } else if (result < result2) {
                                    System.out.println("Due to Sudden Death " + t2.getTeamName() + " Wins!");
                                    return "t2";
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // determines if the penalty has gone in or not
    public static int penaltyShot(Player shooter, Player goalKeeper) {
        Random random = new Random();
        int x = random.nextInt(100);// 0-99
        // shoots a penalty successfully:
        if ((int) shooter.getPlayersPenaltyScore(shooter) >= x) {
            // saves a penalty successfully:
            int y = random.nextInt(100);
            if (goalieSavePenalty(goalKeeper) >= y) {
                return 0;
            }
        }
        return 1;
    }

    // chance of the goalie saving the penalty:
    public static double goalieSavePenalty(Player goalie) {
        double total = 0;
        total += goalie.getFitness();
        total += goalie.getOffsideAdherence();
        total += goalie.getPositioning();
        return Math.round(total * 100) / 10;
    }

    // determine the penalty order:
    private static Player[] penaltyOrder(Team team) {
        ArrayList<Player> playersOrder = new ArrayList<>();
        // store the goalKeeper:
        Player goalKeeper = null;
        // add all the players but the goalkeeper:
        for (int i = 0; i < 11; i++) {
            if (team.getPlayer(i).getPosition().strip().equals("Goal Keeper")) {
                goalKeeper = team.getPlayer(i);
            } else {
                playersOrder.add(team.getPlayer(i));
            }
        }
        // copy contents from ArrayList to another:
        Player[] tempArray = new Player[10];
        for (int i = 0; i < 10; i++) {
            tempArray[i] = playersOrder.get(i);
        }
        // sort that array:
        Player[] sortedTempArray = sortPlayers(tempArray);
        Player[] sortedArray = new Player[11];
        // copy contents into new array
        for (int j = 0; j < 10; j++) {
            sortedArray[j] = sortedTempArray[j];
        }

        // now add the goalkeeper:
        sortedArray[10] = goalKeeper;
        return sortedArray;
    }

    // sort Players:
    public static Player[] sortPlayers(Player[] arr) {
        // length:
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            Player key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j].getPlayersPenaltyScore(arr[j]) > key.getPlayersPenaltyScore(key)) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
        return arr;
    }

    // scored? true false
    private static boolean shotAttempt(int t1ChancesToScore, int t2ChanceToSave, Team t) {
        Random random = new Random();
        String[] randomPhrase = { " gets a shot on goal!", " takes a shot!", " fires it towards the goal!",
                " has a chance to score!" };
        int x = random.nextInt(100);// 0-99
        // shot attempt:
        if (x <= t1ChancesToScore) {

            // random phrase:
            int y = random.nextInt(4);
            System.out.println(t.getTeamName() + randomPhrase[y]);

            // successful shot, now save attempt:
            int z = random.nextInt(100);// 0-99
            if (z <= t2ChanceToSave) {
                System.out.println("What a save by the keeper!");
                return false;
            }
            System.out.println("GOAL!!!");
            return true;
        }
        return false;
    }

    private static double chancesToSave(Team t) {
        double total = 0;
        for (int i = 0; i < 11; i++) {
            // add up the probability
            // (In my opinion these are the factor that would contribute the most)
            Player player = t.getPlayer(i);
            String p1Position = (player.getPosition()).strip();
            if (p1Position.equals("Goal Keeper")) {
                total += player.getFitness();
                total += player.getPassingAccuracy();
                total += player.getPositioning();

            } else if (p1Position.equals("Defender")) {
                total += player.getFitness();
                total += player.getPassingAccuracy();
                total += player.getPositioning();
                total += player.getDefensiveness();
                total += player.getDribbling();
            }
        }

        // add the managers bonus
        Manager manager = t.getManager();
        double managersBonus = manager.getAbility() + manager.getRespect() + manager.getBelief()
                + manager.getKnowledge();
        total += managersBonus;
        return Math.round(total * 10) / 10;
    }

    // calculate the chance to score a goal for each team
    private static double chancesToScore(Team t) {
        // In this case I only take into consideration the midfielders skill level and
        // the forwards skill level
        double total = 0;

        for (int i = 0; i < 11; i++) {
            // add up the probability
            // (In my opinion these are the factor that would contribute the most)
            Player player = t.getPlayer(i);
            String p1Position = (player.getPosition()).strip();
            if (p1Position.equals("Midfielder")) {
                total = total + player.getFitness();
                total = total + player.getPassingAccuracy();
                total = total + player.getPositioning();
                total = total + player.getDribbling();
                total = total + player.getChanceCreation();
            } else if (p1Position.equals("Forward")) {
                total = total + player.getFitness();
                total = total + player.getPositioning();
                total = total + player.getDribbling();
                total = total + player.getChanceCreation();
                total = total + player.getShotAccuracy();
                total = total + player.getShotFrequency();
            }
        }

        // managers bonus:
        Manager manager = t.getManager();
        double managersBonus = manager.getAbility() + manager.getRespect() + manager.getBelief()
                + manager.getKnowledge();
        total += managersBonus;
        return Math.round(total * 10) / 10;
    }

    // randomise the match order:
    public static void randomiseGames(ArrayList<String> array) {
        Collections.shuffle(array);
    }
}