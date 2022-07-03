import java.util.*;
import java.io.*;

public abstract class Jewel {
    public int getPoint(String sign){
        if(sign.equals("D")){
            return 30;
        }
        else if(sign.equals("S")){
            return 15;
        }
        else if(sign.equals("T")){
            return 15;
        }
        else if(sign.equals("W")){
            return 10;
        }
        else{
            return 20;
        }
    }
    public String jewelSign;
    public String getJewelSign(){return this.jewelSign;}
    public void setJewelSign(String newSign){this.jewelSign = newSign;}
    public void emptyJewelSign(){this.jewelSign = " ";}
    public static ArrayList<ArrayList<? extends Jewel>> gameGrid = new ArrayList<>();
    public static ArrayList<ArrayList<? extends Jewel>> getGameGrid(){return gameGrid;}

    @SuppressWarnings("unchecked")
    /* A method that creates jewels depending on the argument provided, suppress warnings for generics casting */
    public static <T extends Jewel> T createJewel(String jewelSign){
        if(jewelSign.equals("D")){
            Diamond jewel = new Diamond();
            return (T) jewel;
        }
        else if(jewelSign.equals("S")){
            Square jewel = new Square();
            return (T) jewel;
        }
        else if(jewelSign.equals("T")){
            Triangle jewel = new Triangle();
            return (T) jewel;
        }
        else if(jewelSign.equals("W")){
            WildCard jewel = new WildCard();
            return (T) jewel;
        }
        else{
            /* if the provided argument is not one of characters mentioned before, it is a math jewel*/
            MathJewel jewel = new MathJewel(jewelSign);
            return (T) jewel;
        }
    }

    /* A method that simply iterates over the static field gameGrid to print jewel signs */
    public static String printGrid(){
        String s ="";
        for(ArrayList<? extends Jewel> list: gameGrid){
            for(int i = 0; i<list.size(); i++){
                if(i == list.size()-1){s = s+(list.get(i)).getJewelSign()+"\n";}
                else{s = s+(list.get(i)).getJewelSign()+" ";}
            }
        }
        return s+"\n";
    }

    /* A method that iterates over the gameGrid to detect empty spaces and slide the jewels down accordingly*/
    public static void slideDown(){
        try {
            for (int i = 0; i < gameGrid.size(); i++) {
                for (int j = 0; j < (gameGrid.get(i)).size(); j++) {
                    if(i != gameGrid.size()-1) {
                        String sign = ((gameGrid.get(i + 1)).get(j)).getJewelSign();
                        if (sign.equals(" ")) {
                            ((gameGrid.get(i + 1)).get(j)).setJewelSign(((gameGrid.get(i)).get(j)).getJewelSign());
                            ((gameGrid.get(i)).get(j)).emptyJewelSign();
                        }
                    }
                }
            }
        }
        catch(IndexOutOfBoundsException e){
            /*already taken precautions inside the try block but used try catch statements for just in case*/
        }
    }

    /* A search method that will later be called inside the direction specific search methods with direction specific arguments */
    private static <T extends Jewel> boolean Search(T jewel, Player p, int row, int x1, int x2, int column, int y1, int y2){
        String jewelSign = jewel.getJewelSign();
        String sign1 = ((gameGrid.get(x1)).get(y1)).getJewelSign();
        String sign2 = ((gameGrid.get(x2)).get(y2)).getJewelSign();
        /* This if statement checks 4 possible jewel match scenarios
        first one: all 3 jewels are the same
        second one: the first jewel is a wildcard and other two jewels are the same
        third one: two jewels are wildcards and the other one is a random jewel that is not a math jewel
        fourth one: all the jewels are not letters, so they are regarded as math jewels*/
        if(((sign1.equals(sign2)) && (sign1.equals(jewelSign))) ||
                ((jewelSign.equals("W")) && (sign1.equals(sign2)) && (Character.isLetter(sign2.charAt(0)))) ||
                ((jewelSign.equals("W")) && ((sign1.equals("W")) || sign2.equals("W")) && ((Character.isLetter(sign1.charAt(0))) && (Character.isLetter(sign2.charAt(0)))))||
                (!(Character.isLetter(jewelSign.charAt(0))) && !(Character.isLetter(sign1.charAt(0))) && !(Character.isLetter(sign2.charAt(0))))){
            /* Emptying jewel signs if they are popped */
            ((gameGrid.get(row)).get(column)).emptyJewelSign();
            ((gameGrid.get(x1)).get(y1)).emptyJewelSign();
            ((gameGrid.get(x2)).get(y2)).emptyJewelSign();
            /* getting points according to jewel signs */
            int p1 = ((gameGrid.get(row)).get(column)).getPoint(jewelSign);
            int p2 = ((gameGrid.get(x1)).get(y1)).getPoint(sign1);
            int p3 = ((gameGrid.get(x2)).get(y2)).getPoint(sign2);
            /* adding the total point to the player */
            p.setScore(p1+p2+p3);
            return true;
        }
        else{
            /* player gets 0 score from this round if there are no matches */
            p.setScore(0);
            return false;
        }
    }

    /* Left diagonal specific search with specific parameters passed to the original search method */
    public static <T extends Jewel> boolean leftDiagonalSearch(T jewel, Player p, int row, int column){
        boolean b = false;
        try{
            try{
                b = Search(jewel,p,row, row-1, row-2,column, column-1, column-2);
            }
            catch(IndexOutOfBoundsException e){
                /* do nothing*/
            }
            /* finally block to check if the first direction search was successful and to take the next action accordingly */
            finally{
                if(b){
                    return true;
                }
                else{
                    b = Search(jewel,p,row, row+1, row+2, column, column+1, column+2);
                    if(b){
                        return true;
                    }
                }
            }
        }
        /* if nothing was true or there were exceptions in both directions, return false */
        catch(IndexOutOfBoundsException e){
            return false;
        }
        return false;
    }

    /* Right diagonal specific search with specific parameters passed to the original search method */
    public static <T extends Jewel> boolean rightDiagonalSearch(T jewel, Player p, int row, int column){
        boolean b = false;
        try{
            try{
                b = Search(jewel,p,row, row-1, row-2,column, column+1, column+2);
            }
            catch(IndexOutOfBoundsException e){
                /* do nothing*/
            }
            /* finally block to check if the first direction search was successful and to take the next action accordingly */
            finally{
                if(b){
                    return true;
                }
                else{
                    b = Search(jewel,p,row, row+1, row+2, column, column-1, column-2);
                    if(b){
                        return true;
                    }
                }
            }
        }
        /* if nothing was true or there were exceptions in both directions, return false */
        catch(IndexOutOfBoundsException e){
            return false;
        }
        return false;
    }

    /* Horizontal specific search with specific parameters passed to the original search method */
    public static <T extends Jewel> boolean horizontalSearch(T jewel, Player p, int row, int column){
        boolean b = false;
        try{
            try{
                b = Search(jewel,p,row, row, row,column, column-1, column-2);
            }
            catch(IndexOutOfBoundsException e){
                /* do nothing*/
            }
            /* finally block to check if the first direction search was successful and to take the next action accordingly */
            finally{
                if(b){
                    return true;
                }
                else{
                    b = Search(jewel,p,row, row, row, column, column+1, column+2);
                    if(b){
                        return true;
                    }
                }
            }
        }
        /* if nothing was true or there were exceptions in both directions, return false */
        catch(IndexOutOfBoundsException e){
            return false;
        }
        return false;
    }

    /* Vertical specific search with specific parameters passed to the original search method */
    public static <T extends Jewel> boolean verticalSearch(T jewel, Player p, int row, int column){
        boolean b = false;
        try{
            try{
                b = Search(jewel,p,row, row-1, row-2,column, column, column);
            }
            catch(IndexOutOfBoundsException e){
                /* do nothing*/
            }
            /* finally block to check if the first direction search was successful and to take the next action accordingly */
            finally{
                if(b){
                    return true;
                }
                else{
                    b = Search(jewel,p,row, row+1, row+2, column, column, column);
                    if(b){
                        return true;
                    }
                }
            }
        }
        /* if nothing was true or there were exceptions in both directions, return false */
        catch(IndexOutOfBoundsException e){
            return false;
        }
        return false;
    }

    /* A method that will be called inside the main.java and according to it's parameters
    and the selected jewels characteristics, direction specific search methods will be called
    Returns false ONLY if the selected jewel is an empty jewel and a warning message is displayed to the monitoring.txt accordingly */
    public static <T extends Jewel> boolean inputProcessor(T jewel, Player p, int row, int column){
        /* Return values of direction specific search methods are used to navigate through multiple possibilites for jewels
        such as D, W and + */
        if((jewel.getJewelSign()).equals("D")){
            boolean b = leftDiagonalSearch(jewel,p,row,column);
            if(!b){
               b = rightDiagonalSearch(jewel,p,row,column);
            }
         }
         else if((jewel.getJewelSign()).equals("S") || (jewel.getJewelSign()).equals("-")){
            boolean b = horizontalSearch(jewel,p,row,column);
         }
         else if(((jewel.getJewelSign()).equals("T") || (jewel.getJewelSign()).equals("|"))){
            boolean b = verticalSearch(jewel,p,row,column);
         }
         else if((jewel.getJewelSign()).equals("W")){
             boolean b = verticalSearch(jewel,p,row,column);
             if(!b){
                 b = horizontalSearch(jewel,p,row,column);
                 if(!b){
                     b = leftDiagonalSearch(jewel,p,row,column);
                     if(!b){
                         b = rightDiagonalSearch(jewel,p,row,column);
                     }
                 }
             }
         }
         else if((jewel.getJewelSign()).equals("+")){
             boolean b = horizontalSearch(jewel,p,row,column);
             if(!b){
                 b = verticalSearch(jewel,p,row,column);
             }
         }
         else if((jewel.getJewelSign()).equals("\\")){
            boolean b = leftDiagonalSearch(jewel,p,row,column);
         }
         else if((jewel.getJewelSign()).equals("/")){
             boolean b = rightDiagonalSearch(jewel,p,row,column);
         }
         else if((jewel.getJewelSign()).equals(" ")){
             return false;
         }
         return true;
    }

    public static void main(String[] args) {}
}
class Diamond extends Jewel{
    public String jewelSign = "D";
    public String getJewelSign(){return this.jewelSign;}
    public void setJewelSign(String newSign){this.jewelSign = newSign;}
    public void emptyJewelSign(){this.jewelSign = " ";}
}
class Square extends Jewel{
    public String jewelSign = "S";
    public String getJewelSign(){return this.jewelSign;}
    public void setJewelSign(String newSign){this.jewelSign = newSign;}
    public void emptyJewelSign(){this.jewelSign = " ";}
}
class Triangle extends Jewel{
    public String jewelSign = "T";
    public String getJewelSign(){return this.jewelSign;}
    public void setJewelSign(String newSign){this.jewelSign = newSign;}
    public void emptyJewelSign(){this.jewelSign = " ";}
}
class WildCard extends Jewel{
    public String jewelSign = "W";
    public String getJewelSign(){return this.jewelSign;}
    public void setJewelSign(String newSign){this.jewelSign = newSign;}
    public void emptyJewelSign(){this.jewelSign = " ";}
}
class MathJewel extends Jewel{
    public String jewelSign;
    public String getJewelSign(){return this.jewelSign;}
    public void setJewelSign(String newSign){this.jewelSign = newSign;}
    public void emptyJewelSign(){this.jewelSign = " ";}
    public MathJewel(String sign){
        this.jewelSign = sign;
    }
}