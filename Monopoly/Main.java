import java.io.*;
import java.util.*;

public class Main{

    public static void main(String[] args) {
        try {
            JsonReader.initializeGameBoard(); /* Creating the gameBoard by calling the static method of JsonReader */
            BufferedReader command = new BufferedReader(new FileReader(args[0]));
            String line;
            BufferedWriter monitoring = new BufferedWriter(new FileWriter("monitoring.txt"));
            Banker banker = new Banker();
            Player player1 = new Player("Player 1");
            Player player2 = new Player("Player 2");
            ArrayList<Object> gameBoard = Board.getGameBoard();

            /* while command file has a next line and no player goes bankrupt*/
            while(((line = command.readLine()) != null) && (player1.isBankrupt() == false) && (player2.isBankrupt() == false) && (banker.isBankrupt() == false)){
                line = line.replace("\n", ",");
                String[] lineList = line.split(";");
                if(lineList.length > 1) /* if lineList length is greater than 1, this indicates the command is PlayerName;Dice*/{
                    int dice = Integer.parseInt(lineList[1]);
                    if(lineList[0].equals("Player 1")){
                        if((player1.getJail() == false) && (player1.getParking() == false)) /* if player is not in jail or free parking */{
                            player1.setCurrentPosition(dice, banker);
                            int squareNum = player1.getCurrentPosition();
                            if (squareNum == 8 || squareNum == 23 || squareNum == 37) {
                                ArrayList<Chance> myList = (ArrayList<Chance>) gameBoard.get(squareNum -1);
                                monitoring.write(Chance.chanceCardSquare(player1, player2, banker, myList, gameBoard, dice));
                            }
                            else if (squareNum == 3 || squareNum == 18 || squareNum == 34) {
                                ArrayList<CommunityChest> myList = (ArrayList<CommunityChest>) gameBoard.get(squareNum -1);
                                monitoring.write(CommunityChest.communityChestCardSquare(player1, player2, banker, myList, dice));
                            }
                            else if(squareNum == 1 || squareNum == 5 || squareNum == 11 || squareNum == 21 || squareNum == 31 || squareNum == 39){
                                Action a = (Action) gameBoard.get(squareNum -1);
                                monitoring.write(Action.actionSquare(player1 ,player2 ,banker ,a ,dice));
                            }
                            else{
                                Property p = (Property) gameBoard.get(squareNum-1);
                                monitoring.write(Property.propertySquare(p,player1,player2,banker,dice));
                            }
                        }
                        else{
                            if(player1.getParking() == true) /* if player is in free parking*/ {
                                String myStr = String.format("%s\t%d\t%d\t%d\t%d\t%s in free parking (count=%d)\n",
                                    player1.getPlayerName(),
                                    dice,
                                    player1.getCurrentPosition(),
                                    player1.getAccountBalance(),
                                    player2.getAccountBalance(),
                                    player1.getPlayerName(),
                                    player1.getParkingTime());
                                monitoring.write(myStr);
                                player1.parkingCounter();
                            }
                            else {
                                /* if player is in jail*/
                                String myStr = String.format("%s\t%d\t%d\t%d\t%d\t%s in jail (count=%d)\n",
                                    player1.getPlayerName(),
                                    dice,
                                    player1.getCurrentPosition(),
                                    player1.getAccountBalance(),
                                    player2.getAccountBalance(),
                                    player1.getPlayerName(),
                                    player1.getJailTime());
                                monitoring.write(myStr);
                                player1.jailCounter();
                            }
                        }
                    }
                    else{
                        if((player2.getJail() == false) && (player2.getParking() == false)) /* if player is not in jail or free parking */ {
                            player2.setCurrentPosition(dice, banker);
                            int squareNum = player2.getCurrentPosition();
                            if (squareNum == 8 || squareNum == 23 || squareNum == 37) {
                                ArrayList<Chance> myList = (ArrayList<Chance>) gameBoard.get(squareNum -1);
                                monitoring.write(Chance.chanceCardSquare(player2, player1, banker, myList, gameBoard, dice));
                            }
                            else if (squareNum == 3 || squareNum == 18 || squareNum == 34) {
                                ArrayList<CommunityChest> myList = (ArrayList<CommunityChest>) gameBoard.get(squareNum -1);
                                monitoring.write(CommunityChest.communityChestCardSquare(player2, player1, banker, myList, dice));
                            }
                            else if(squareNum == 1 || squareNum == 5 || squareNum == 11 || squareNum == 21 || squareNum == 31 || squareNum == 39){
                                Action a = (Action) gameBoard.get(squareNum -1);
                                monitoring.write(Action.actionSquare(player2 ,player1 ,banker ,a ,dice));
                            }
                            else{
                                Property p = (Property) gameBoard.get(squareNum-1);
                                monitoring.write(Property.propertySquare(p,player2,player1,banker,dice));
                            }
                        }
                        else{
                            if(player2.getParking() == true)/* if player is in free parking*/ {
                                String myStr = String.format("%s\t%d\t%d\t%d\t%d\t%s in free parking (count=%d)\n",
                                    player2.getPlayerName(),
                                    dice,
                                    player2.getCurrentPosition(),
                                    player1.getAccountBalance(),
                                    player2.getAccountBalance(),
                                    player2.getPlayerName(),
                                    player2.getParkingTime());
                                monitoring.write(myStr);
                                player2.parkingCounter();
                            }
                            else {
                                /* if player is in jail*/
                                String myStr = String.format("%s\t%d\t%d\t%d\t%d\t%s in jail (count=%d)\n",
                                    player2.getPlayerName(),
                                    dice,
                                    player2.getCurrentPosition(),
                                    player1.getAccountBalance(),
                                    player2.getAccountBalance(),
                                    player2.getPlayerName(),
                                    player2.getJailTime());
                                monitoring.write(myStr);
                                player2.jailCounter();
                            }
                        }
                    }
                }
                else{
                    /* if lineList length isn't greater than 1, this indicates the command is show()*/
                    monitoring.write("-----------------------------------------------------------------------------------------------------------\n");
                    String player1Properties = (player1.getPlayerProperties()).toString();
                    player1Properties = player1Properties.replace("[", "");
                    player1Properties = player1Properties.replace("]", "");
                    String player2Properties = (player2.getPlayerProperties()).toString();
                    player2Properties = player2Properties.replace("[", "");
                    player2Properties = player2Properties.replace("]", "");
                    /*Both player properties are converted to string in order to print them*/
                    String myStr = String.format("%s\t%d\t have: %s\n%s\t%d\t have: %s\nBanker\t%d\nWinner %s\n",
                            player1.getPlayerName(),
                            player1.getAccountBalance(),
                            player1Properties,
                            player2.getPlayerName(),
                            player2.getAccountBalance(),
                            player2Properties,
                            banker.getAccountBalance(),
                            (player1.getAccountBalance() > player2.getAccountBalance()) ? "Player 1" : "Player 2");
                    monitoring.write(myStr);
                    monitoring.write("-----------------------------------------------------------------------------------------------------------\n");
                }
            }
            /* Show() command is placed at the end so when the while loop ends, either because a player goes bankrupt or because commands.txt is finished, a show() command is executed as the final command*/
            monitoring.write("-----------------------------------------------------------------------------------------------------------\n");
            String player1Properties = (player1.getPlayerProperties()).toString();
            player1Properties = player1Properties.replace("[", "");
            player1Properties = player1Properties.replace("]", "");
            String player2Properties = (player2.getPlayerProperties()).toString();
            player2Properties = player2Properties.replace("[", "");
            player2Properties = player2Properties.replace("]", "");
            /*Both player properties are converted to string in order to print them*/
            String myStr = String.format("%s\t%d\t have: %s\n%s\t%d\t have: %s\nBanker\t%d\nWinner %s\n",
                    player1.getPlayerName(),
                    player1.getAccountBalance(),
                    player1Properties,
                    player2.getPlayerName(),
                    player2.getAccountBalance(),
                    player2Properties,
                    banker.getAccountBalance(),
                    (player1.getAccountBalance() > player2.getAccountBalance()) ? "Player 1" : "Player 2");
            monitoring.write(myStr);
            monitoring.write("-----------------------------------------------------------------------------------------------------------\n");
            monitoring.close();
        }
        catch (IOException x) {System.err.format("IOException: %s%n", x);}
    }
}