import java.io.*;
import java.util.*;

public abstract class Players{ /* Abstract class players has the common methods and fields of banker and player*/
    public int accountBalance;
    public int getAccountBalance(){return this.accountBalance;}
    public void addMoney(int x){this.accountBalance += x;}
    public void subtractMoney(int y){this.accountBalance -= y;}

    public boolean bankruptcy = false;
    public void setBankruptcy(){this.bankruptcy = true;}
    public boolean isBankrupt(){return this.bankruptcy;}

    public static void main(String[] args) {}
}
class Banker extends Players{
    public int accountBalance = 100000;
    @Override
    public int getAccountBalance(){return this.accountBalance;}
    @Override
    public void addMoney(int x){this.accountBalance += x;}
    @Override
    public void subtractMoney(int y){this.accountBalance -= y;}

    public boolean bankruptcy = false;
    @Override
    public void setBankruptcy(){this.bankruptcy = true;}
    @Override
    public boolean isBankrupt(){return this.bankruptcy;}

    public static void main(String[] args) {}

}
class Player extends Players{
    public String playerName;
    public String getPlayerName(){return this.playerName;}

    public int accountBalance = 15000;
    @Override
    public int getAccountBalance(){return this.accountBalance;}
    @Override
    public void addMoney(int x){this.accountBalance += x;}
    @Override
    public void subtractMoney(int y){this.accountBalance -= y;}

    public boolean bankruptcy = false;
    @Override
    public void setBankruptcy(){this.bankruptcy = true;}
    @Override
    public boolean isBankrupt(){return this.bankruptcy;}

    public boolean inJail = false;
    public int jailTime = 1;
    public int getJailTime(){return this.jailTime;}
    public void setJail(boolean b){this.inJail = b;}
    public boolean getJail(){return this.inJail;}
    public void jailCounter(){ /* Method to keep track of jail time*/
        if(this.inJail == true){
            if(this.jailTime == 3){
                this.inJail = false;
            }
            else{this.jailTime++;}
        }
    }

    public boolean inFreeParking = false;
    public int parkingTime = 1;
    public int getParkingTime(){return this.parkingTime;}
    public void setParking(boolean b){this.inFreeParking = b;}
    public boolean getParking(){return this.inFreeParking;}
    public void parkingCounter(){ /* Method to keep track of free parking time*/
        if(this.inFreeParking == true){
            if(this.parkingTime == 1){
                this.inFreeParking = false;
            }
            else{this.parkingTime++;}
        }
    }

    public ArrayList<String> playerProperties = new ArrayList<String>(); /* Arraylist to store player owned properties*/
    public ArrayList<String> getPlayerProperties(){return this.playerProperties;}
    public void addProperty(String s){this.playerProperties.add(s);}
    public ArrayList<Property> playerRailRoads = new ArrayList<Property>(); /* Arraylist to store player owned rail roads to later use when calculating rent*/
    public ArrayList<Property> getPlayerRailRoads(){return this.playerRailRoads;}
    public int playerOwnedRailRoads(){return (this.playerRailRoads).size();}
    public int currentPosition = 1;
    public int getCurrentPosition(){return this.currentPosition;}
    public void setCurrentPosition(int z, Banker b){
        if ((this.currentPosition + z )>40){
            this.currentPosition = (this.currentPosition + z)%40;
            this.accountBalance += 200;
            b.accountBalance -= 200; /* This part is used when a player advances to the "GO" square from the last square*/
        }
        else{this.currentPosition += z;}
    }
    public void instantSetPosition(int i){this.currentPosition = i;} /* This method is used to set a players position instantly in the case of "Advance to Leicester Square" or "Go back 3 spaces" */
    public Player(String name){this.playerName = name;}

    public static void main(String[] args) {}

}