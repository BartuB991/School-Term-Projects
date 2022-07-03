import java.io.*;
import java.util.*;

public abstract class Board{
    public static ArrayList<Object> gameBoard = new ArrayList<Object>(); /*ArrayList to store squares according to their original order*/
    public int place; /*Field that indicates specific places of the squares, used only when putting objects into gameBoard ArrayList*/
    public static ArrayList<Object> getGameBoard(){return gameBoard;}

    public static void main(String[] args) {}
}
class Property extends Board{
    /* Method that performs property square related actions */
    public static String propertySquare(Property p, Player playing, Player waiting, Banker b, int dice) {
        int price = p.getPropertyCost();
        String s = null;
        String propertyName = p.getPropertyName();
        ArrayList<String> playerPropertyNames = playing.getPlayerProperties(); /* ArrayList that stores the player owned property names in order to print them later */
        if((p.hasOwner() == true) && ((playerPropertyNames.contains(propertyName) == false))){ /*if subject property has an owner and this owner is not the current player*/
            int rent;
            if((p.getPropertyType()).equals("Land")){
                if(price <= 2000){
                    rent = price*40/100;
                    if(rent > playing.getAccountBalance()){
                        playing.setBankruptcy(); /* player goes bankrupt if she/he can't pay the rent*/
                    }
                    else {
                        playing.subtractMoney(rent);
                        waiting.addMoney(rent);
                    }
                }
                else if(price <= 3000){
                    rent = price*30/100;
                    if(rent > playing.getAccountBalance()){
                        playing.setBankruptcy(); /* player goes bankrupt if she/he can't pay the rent*/
                    }
                    else {
                        playing.subtractMoney(rent);
                        waiting.addMoney(rent);
                    }
                }
                else{
                    rent = price*35/100;
                    if(rent > playing.getAccountBalance()){
                        playing.setBankruptcy(); /* player goes bankrupt if she/he can't pay the rent*/
                    }
                    else {
                        playing.subtractMoney(rent);
                        waiting.addMoney(rent);
                    }
                }
            }
            else if((p.getPropertyType()).equals("Rail Road")){ /* Special rent calculator for Rail Roads*/
                rent = 25*waiting.playerOwnedRailRoads();
                if(rent > playing.getAccountBalance()){
                    playing.setBankruptcy(); /* player goes bankrupt if she/he can't pay the rent*/
                }
                else {
                    playing.subtractMoney(rent);
                    waiting.addMoney(rent);
                }
            }
            else{ /* Special rent calculator for Companies*/
                rent = 4*dice;
                if(rent > playing.getAccountBalance()){
                    playing.setBankruptcy(); /* player goes bankrupt if she/he can't pay the rent*/
                }
                else {
                    playing.subtractMoney(rent);
                    waiting.addMoney(rent);
                }
            }
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s paid rent for %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    p.getPropertyName() /* String that is returned when a player pays rent*/
            );
        }
        else if(((p.hasOwner() == true) && ((playerPropertyNames.contains(propertyName) == true)))){ /* if the subject property has an owner and this owner is the player that is currently playing*/
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s has %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    p.getPropertyName() /* String that is returned when a player already owns the square*/
            );
        }
        else{
            if(playing.getAccountBalance() >= price){ /* Player buys the property if she/he has enough money*/
                playing.addProperty(p.getPropertyName());
                p.setOwner(playing);
                playing.subtractMoney(price);
                b.addMoney(price);
                if((p.getPropertyType()).equals("Rail Road")){
                    (playing.getPlayerRailRoads()).add(p);
                }
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s bought %s\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        (playing.getPlayerName().equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName(),
                        p.getPropertyName() /* String that is returned when a player buys the square*/
                );
            }
            else{
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        (playing.getPlayerName().equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName()); /* String that is returned when a player can't buy the square*/
                playing.setBankruptcy(); /* player goes bankrupt if she/he can't buy the square*/
            }
        }
        return s;
    }

    public Player owner;
    public void setOwner(Player owner){this.owner = owner;}
    public Player getOwner(){return this.owner;}
    public boolean hasOwner(){return this.owner != null;}
    public String propertyType, propertyName;
    public String getPropertyType(){return this.propertyType;}
    public String getPropertyName(){return this.propertyName;}
    public int propertyCost;
    public int getPropertyCost(){return this.propertyCost;}
    public Property(int place, String propertyType, String propertyName, int propertyCost){
        this.place = place; this.propertyType = propertyType; this.propertyName = propertyName; this.propertyCost = propertyCost; this.owner = null;
    }

    public static void main(String[] args) {}
}
class Chance extends Board{
    public static String chanceCardSquare(Player playing, Player waiting, Banker b, ArrayList<Chance> myList, ArrayList<Object> myBoard, int dice){
        Chance chanceCard = myList.get(0); /* Returns the card that is next in the order*/
        String cardStatement =chanceCard.getCardStatement(); /* Returns the statement of the card */
        myList.remove(0); /* Since we already have the card information, we can remove the card from the pile so that the same card isn't used more than once*/
        String s = null;
        if( cardStatement.equals("Advance to Go (Collect $200)")){
            playing.addMoney(200);
            b.subtractMoney(200);
            playing.instantSetPosition(1);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Chance - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        else if( cardStatement.equals("Advance to Leicester Square")){
            playing.instantSetPosition(27);
            Property p = (Property) myBoard.get(playing.getCurrentPosition()-1);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Chance - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
            return Property.propertySquare(p,playing,waiting,b,dice); /* Function call to perform Leicester Square actions*/
        }
        else if(cardStatement.equals("Go back 3 spaces")){playing.setCurrentPosition(-3,b);
            if(playing.getCurrentPosition() == 8 || playing.getCurrentPosition() == 23 || playing.getCurrentPosition() == 37){
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Chance - %s\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName(),
                        cardStatement);
                return Chance.chanceCardSquare(playing, waiting,b,(ArrayList<Chance>)(myBoard.get(playing.getCurrentPosition()-1)), myBoard, dice); /* Chance square function is returned if player lands ona Chance square when going back 3 places*/
            }
            else if(playing.getCurrentPosition() == 3 || playing.getCurrentPosition() == 18 || playing.getCurrentPosition() == 34){
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Chance - %s\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName(),
                        cardStatement);
                return CommunityChest.communityChestCardSquare(playing, waiting,b,(ArrayList<CommunityChest>)(myBoard.get(playing.getCurrentPosition()-1)),dice); /* CommunityChest square function is returned if player lands ona CommunityChest square when going back 3 places*/
            }
            else if(playing.getCurrentPosition() == 1 || playing.getCurrentPosition() == 5 || playing.getCurrentPosition() == 11 || playing.getCurrentPosition() == 21 || playing.getCurrentPosition() == 31 || playing.getCurrentPosition() == 39){
                Action a = (Action) (myBoard.get(playing.getCurrentPosition()-1));
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Chance - %s\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName(),
                        cardStatement);
                return Action.actionSquare(playing,waiting,b,a,dice); /* Action square function is returned if player lands ona Action square when going back 3 places*/
            }
            else{
                Property p = (Property) myBoard.get(playing.getCurrentPosition()-1);
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Chance - %s\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName(),
                        cardStatement);
                return Property.propertySquare(p,playing,waiting,b,dice); /* Property square function is returned if player lands ona Property square when going back 3 places*/
            }
        }
        else if(cardStatement.equals("Pay poor tax of $15")){playing.subtractMoney(15); b.addMoney(15);
            if(playing.getAccountBalance() < 15){
                playing.setBankruptcy();
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName());
            }
            else {
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Chance - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
            }
        }
        else if(cardStatement.equals("Your building loan matures - collect $150")){ playing.addMoney(150); b.subtractMoney(150);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Chance - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        else if(cardStatement.equals("You have won a crossword competition - collect $100 ")){playing.addMoney(100); b.subtractMoney(100);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Chance - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        return s;
    }

    public static ArrayList<Chance> chanceCards = new ArrayList<Chance>(); /* Arraylist to store chance cards */
    public static ArrayList<Chance> getChanceCards() {return chanceCards;}
    public String cardStatement;
    public String getCardStatement(){return this.cardStatement;}
    public Chance(String cardStatement){
        this.cardStatement = cardStatement;
        chanceCards.add(this);
    }

    public static void main(String[] args) {}
}
class CommunityChest extends Board{
    public static String communityChestCardSquare(Player playing, Player waiting, Banker b, ArrayList<CommunityChest> myList, int dice){
        CommunityChest communityChestCard = myList.get(0); /* Returns the card that is next in the order*/
        String cardStatement = communityChestCard.getCardStatement(); /* Returns the statement of the card */
        myList.remove(0); /* Since we already have the card information, we can remove the card from the pile so that the same card isn't used more than once*/
        String s = null;
        if(cardStatement.equals("Advance to Go (Collect $200)")) {
            playing.addMoney(200);
            b.subtractMoney(200);
            playing.instantSetPosition(1);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        else if(cardStatement.equals("Bank error in your favor - collect $75")){ playing.addMoney(75); b.subtractMoney(75);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        else if(cardStatement.equals("Doctor's fees - Pay $50")){ playing.subtractMoney(50); b.addMoney(50);
            if(playing.getAccountBalance() < 50){
                playing.setBankruptcy();
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName());
            }
            else {
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName(),
                        cardStatement);
            }
        }
        else if(cardStatement.equals("It is your birthday Collect $10 from each player")){playing.addMoney(10); waiting.subtractMoney(10);
            if(waiting.getAccountBalance() < 10){
                waiting.setBankruptcy();
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        waiting.getPlayerName());
            }
            else {
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName(),
                        cardStatement);
            }
        }
        else if(cardStatement.equals("Grand Opera Night - collect $50 from every player for opening night seats")){playing.addMoney(50); waiting.subtractMoney(50);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        else if(cardStatement.equals("Income Tax refund - collect $20")){playing.addMoney(20); b.subtractMoney(20);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        else if(cardStatement.equals("Life Insurance Matures - collect $100")){playing.addMoney(100); b.subtractMoney(100);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        else if(cardStatement.equals("Pay Hospital Fees of $100")){playing.subtractMoney(100); b.addMoney(100);
            if(playing.getAccountBalance() < 100){
                playing.setBankruptcy();
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName());
            }
            else {
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
            }
        }
        else if(cardStatement.equals("Pay School Fees of $50")){playing.subtractMoney(50); b.addMoney(50);
            if(playing.getAccountBalance() < 50){
                playing.setBankruptcy();
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName());
            }
            else {
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName(),
                        cardStatement);
            }
        }
        else if(cardStatement.equals("You inherit $100")){playing.addMoney(100); b.subtractMoney(100);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        else if(cardStatement.equals("From sale of stock you get $50")){playing.addMoney(50); b.subtractMoney(50);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s draw Community Chest - %s\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName(),
                    cardStatement);
        }
        return s;
    }

    public static ArrayList<CommunityChest> communityChestCards = new ArrayList<CommunityChest>(); /* Arraylist to store CommunityChest cards */
    public static ArrayList<CommunityChest> getCommunityChestCards() {return communityChestCards;}
    public String cardStatement;
    public String getCardStatement(){return this.cardStatement;}
    public CommunityChest(String cardStatement){
        this.cardStatement = cardStatement;
        communityChestCards.add(this);
    }

    public static void main(String[] args) {}
}
class Action extends Board{
    public static String actionSquare(Player playing, Player waiting, Banker b, Action a, int dice){
        String s = null;
        if((a.getActionType()).equals("GO")){
            b.subtractMoney(200);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s is in GO square\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName());
        }
        else if((a.getActionType()).equals("Income Tax")){
            if(playing.getAccountBalance() < 100){
                playing.setBankruptcy();
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName());
            }
            else {
                playing.subtractMoney(100);
                b.addMoney(100);
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s paid Tax\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName());
            }
        }
        else if((a.getActionType()).equals("Jail")){
            playing.setJail(true);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s went to jail\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName());
        }
        else if((a.getActionType()).equals("Free Parking")){
            playing.setParking(true);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s is in Free Parking\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName());
        }
        else if((a.getActionType()).equals("Go To Jail")){
            playing.instantSetPosition(11);
            playing.setJail(true);
            playing.setJail(true);
            s = String.format("%s\t%d\t%d\t%d\t%d\t%s went to jail\n",
                    playing.getPlayerName(),
                    dice,
                    playing.getCurrentPosition(),
                    ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                    playing.getPlayerName());
        }
        else if((a.getActionType()).equals("Super Tax")){
            if(playing.getAccountBalance() < 100){
                playing.setBankruptcy();
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName());
            }
            else {
                playing.subtractMoney(100);
                b.addMoney(100);
                s = String.format("%s\t%d\t%d\t%d\t%d\t%s paid Tax\n",
                        playing.getPlayerName(),
                        dice,
                        playing.getCurrentPosition(),
                        ((playing.getPlayerName()).equals("Player 1")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        ((playing.getPlayerName()).equals("Player 2")) ? playing.getAccountBalance() : waiting.getAccountBalance(),
                        playing.getPlayerName());
            }
        }
        return s;
    }

    public String actionType;
    public String getActionType(){return this.actionType;}
    public Action(String actionType){ this.actionType=actionType;}

    public static void main(String[] args) {}
}

