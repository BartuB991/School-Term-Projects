import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        try{
            BufferedReader gridReader = new BufferedReader(new FileReader(args[0]));
            String gridLine;
            ArrayList<ArrayList<? extends Jewel>> gameGrid = Jewel.getGameGrid();

            BufferedReader commandReader = new BufferedReader(new FileReader(args[1]));
            String commandString;

            BufferedReader leaderboardReader = new BufferedReader(new FileReader("leaderboard.txt"));
            String leaderboardLine;
            BufferedWriter leaderBoardWriter = new BufferedWriter(new FileWriter("leaderboard.txt", true));

            BufferedWriter monitoring = new BufferedWriter(new FileWriter("monitoring.txt"));

            Player p = new Player();

            /* Reading and creating the grid */
            while((gridLine = gridReader.readLine()) != null){
                gridLine = gridLine.replace("\n","");
                String[] lineList = gridLine.split(" ");
                ArrayList<? extends Jewel> jewels = new ArrayList<>();
                for(String E: lineList){
                    jewels.add(Jewel.createJewel(E));
                }
                gameGrid.add(jewels);
            }
            monitoring.write("Game Grid:\n\n"+Jewel.printGrid());

            /* Reading and creating the players */
            ArrayList<Player> players = new ArrayList<>();
            while((leaderboardLine = leaderboardReader.readLine()) != null){
                leaderboardLine = leaderboardLine.replace("\n", "");
                String[] leaderboardList = leaderboardLine.split(" ");
                Player existingPlayer = new Player(leaderboardList[0],Integer.parseInt(leaderboardList[1]));
                players.add(existingPlayer);
            }

            while((commandString = commandReader.readLine()) != null){
                commandString = commandString.replace("\n","");
                String[] commandList = commandString.split(" ");
                monitoring.write("Select coordinate or enter E to end the game: "+commandString+"\n\n");
                /* If condition for ending the game */
                if(commandString.equals("E")){
                    /* Getting the player name and the result */
                    p.setName((commandReader.readLine()).replace("\n",""));
                    monitoring.write(p.getTotalScore()+p.getName());
                    players.add(p);
                    monitoring.write(Player.playerResult(players,p));
                    monitoring.close();
                    break;
                }
                else{
                    /* Regarding anything other than E as commands and performing necessary actions */
                    int row = Integer.parseInt(commandList[0]);
                    int column = Integer.parseInt(commandList[1]);
                    if((row < gameGrid.size()) && (column < (gameGrid.get(row)).size())){
                        boolean b = Jewel.inputProcessor((gameGrid.get(row)).get(column), p, row, column);
                        for (ArrayList<? extends Jewel> rows : gameGrid) {
                            for (int i = 0; i < rows.size(); i++) {
                                Jewel.slideDown();
                            }
                        }
                        if (!b) {
                            monitoring.write("Please enter a valid coordinate\n\n");
                        } else {
                            monitoring.write(Jewel.printGrid() + p.getCurrentScore());
                        }
                    }
                }
            }
            /* Printing the player to leaderboard.txt */
            String s = String.format("\n%s %d",p.getNameForResult(), p.scoreForComparison());
            leaderBoardWriter.append(s);
            leaderBoardWriter.close();
        }
        catch (IOException x){System.err.format("IOException: %s%n", x);}
    }
}
