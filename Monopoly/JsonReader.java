import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonReader {
    public static void initializeGameBoard(){
        JSONParser parser = new JSONParser();
        try{
        HashMap<Integer, Object> hm = new HashMap<Integer, Object>(); /* hashmap stores squares with their id's and we later take squares using their id's and put them into gameBoard in the correct order */
        BufferedReader listReader = new BufferedReader(new FileReader("list.json"));
        BufferedReader propertyReader = new BufferedReader(new FileReader("property.json"));

        JSONObject listObject = (JSONObject) parser.parse(listReader);
        JSONArray chanceList = (JSONArray) listObject.get("chanceList");
        JSONArray communityChestList = (JSONArray) listObject.get("communityChestList");

        ArrayList<Chance> chanceCardList = new ArrayList<Chance>();
        for (Object i : chanceList) {
            /* Chance object created and added to the corresponding arraylist, the chance arraylist will be later added to it's respective squares in the gameBoard*/
            Chance chanceObj = new Chance((String) ((JSONObject) i).get("item"));
            chanceCardList.add(chanceObj);
        }
        ArrayList<CommunityChest> communityChestCardList = new ArrayList<CommunityChest>();
        for (Object j : communityChestList) {
            /* CommunityChest object created and added to the corresponding arraylist, the communityChance arraylist will be later added to it's respective squares in the gameBoard*/
            CommunityChest chestObj = new CommunityChest((String) ((JSONObject) j).get("item"));
            communityChestCardList.add(chestObj);
        }

        JSONObject propertyObject = (JSONObject) parser.parse(propertyReader);
        JSONArray lands = (JSONArray) propertyObject.get("1");
        JSONArray railRoads = (JSONArray) propertyObject.get("2");
        JSONArray companies = (JSONArray) propertyObject.get("3");

        /* Property objects created and placed into the hashmap with their square id's for later use */
        for (Object k : lands) {
            Property propertyObj = new Property(Integer.parseInt((String) ((JSONObject) k).get("id")), "Land", (String) ((JSONObject) k).get("name"), Integer.parseInt((String) ((JSONObject) k).get("cost")));
            hm.put(Integer.parseInt((String) ((JSONObject) k).get("id")), propertyObj);
        }
        for (Object l : railRoads) {
            Property railRoadObj = new Property(Integer.parseInt((String) ((JSONObject) l).get("id")), "Rail Road", (String) ((JSONObject) l).get("name"), Integer.parseInt((String) ((JSONObject) l).get("cost")));
            hm.put(Integer.parseInt((String) ((JSONObject) l).get("id")), railRoadObj);
        }
        for (Object m : companies) {
            Property companyObj = new Property(Integer.parseInt((String) ((JSONObject) m).get("id")), "Company", (String) ((JSONObject) m).get("name"), Integer.parseInt((String) ((JSONObject) m).get("cost")));
            hm.put(Integer.parseInt((String) ((JSONObject) m).get("id")), companyObj);
        }
        ArrayList<Object> myList = Board.getGameBoard();
        /* Every single object is added to the gameBoard according to it's original place*/
        for (int o = 1; o <= 50; o++) {
            if (o == 1) {
                Action go = new Action("GO");
                myList.add(go);
            } else if (o == 5) {
                Action incomeTax = new Action("Income Tax");
                myList.add(incomeTax);
            } else if (o == 11) {
                Action jail = new Action("Jail");
                myList.add(jail);
            } else if (o == 21) {
                Action freeParking = new Action("Free Parking");
                myList.add(freeParking);
            } else if (o == 31) {
                Action goToJail = new Action("Go To Jail");
                myList.add(goToJail);
            } else if (o == 39) {
                Action superTax = new Action("Super Tax");
                myList.add(superTax);
            } else if (o == 8 || o == 23 || o == 37) {
                myList.add(chanceCardList);
            } else if (o == 3 || o == 18 || o == 34) {
                myList.add(communityChestCardList);
            } else {
                Property p = (Property) hm.get(o);
                myList.add(p);
            }
        }
    } catch (Exception e) {e.printStackTrace();}}
    public static void main(String[] args) {}
}
