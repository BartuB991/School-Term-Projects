import java.util.*;
import java.io.*;

public class Player implements Comparable<Player> {
    public String name; public int totalScore = 0; public int currentScore = 0;
    /* Some accessor methods are 'a little' different from the regular practice
    in order to organise the monitoring.txt easier */
    public void setName(String s){this.name = s;}
    public String getName(){return "Enter name: " + this.name+"\n\n";}
    public String getNameForResult(){return this.name;}
    public void setScore(int i){this.totalScore += i; this.currentScore = i;}
    public String getTotalScore(){return "Score: " + this.totalScore + " points\n\n";}
    public int scoreForComparison(){return this.totalScore;}
    public String getCurrentScore(){return "Score: " + this.currentScore + " points\n\n";}

    /* Overridden compareTo method to sort the Players ArrayList according to the player scores */
    @Override
    public int compareTo(Player p){
        Integer score1 = this.scoreForComparison();
        Integer score2 = p.scoreForComparison();
        /* Negated the return value in order to achieve ascending order */
        return -(score1.compareTo(score2));
    }

    /* A method that sorts the players and performs binary search to find the player that is currently playing
    and then returns a string that is containing the players final result */
    public static String playerResult(ArrayList<Player> players, Player p){
        Collections.sort(players);
        int playerIndex = Collections.binarySearch(players,p);
        String s;
        if(playerIndex == players.size()-1){
            Player player1 = players.get(playerIndex-1);
            s = String.format("Your rank is %d/%d, your score is %d points lower than %s\n\nGood bye!",
                    playerIndex+1,players.size(),(player1.scoreForComparison()-p.scoreForComparison()), player1.getNameForResult());
        }
        else if(playerIndex == 0){
            Player player1 = players.get(playerIndex+1);
            s = String.format("Your rank is %d/%d, your score is %d points higher than %s\n\nGood bye!",
                    playerIndex+1,players.size(),(p.scoreForComparison()-player1.scoreForComparison()), player1.getNameForResult());
        }
        else{
            Player player1 = players.get(playerIndex-1);
            Player player2 = players.get(playerIndex+1);
            s = String.format("Your rank is %d/%d, your score is %d points lower than %s and %d points higher than %s\n\nGood bye!",
                    playerIndex+1, players.size(),(player1.scoreForComparison()-p.scoreForComparison()),player1.getNameForResult(),
                    (p.scoreForComparison()-player2.scoreForComparison()),player2.getNameForResult());
        }
        return s;
    }

    /*Non argument constructor for the player that is currently playing */
    public Player(){

    }

    /* Constructor with arguments for players that are already existing in the leaderboard.txt */
    public Player(String name, int score){
        this.name = name; this.totalScore = score;
    }

    public static void main(String[] args) {}
}
