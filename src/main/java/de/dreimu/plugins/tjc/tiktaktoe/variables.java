package de.dreimu.plugins.tjc.tiktaktoe;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class variables {
    public static HashMap<String,ArrayList<Integer>> verteilung;

    public static HashMap<Player,String> PlayerID;

    public static HashMap<String,String> guiTypeToName() {
        HashMap<String,String> guiTypeToName = new HashMap<String,String>();
        guiTypeToName.put("main","Hauptmenü");
        guiTypeToName.put("game","Spiel gegen ");
        return guiTypeToName;
    }
    
    public static ArrayList<String> guiTypeToArray(String GUItype, String ID) {

        ArrayList<String> aufbau = new ArrayList<String>();

        switch (GUItype) {
            case "game":
                gameArrayList(aufbau, ID);
                break;
            case "main":
                mainArrayList(aufbau);
                break;
            default:
                mainArrayList(aufbau);
                break;
            
                
        }

        return aufbau;
    } public static ArrayList<String> guiTypeToArray(String GUItype) {
        return guiTypeToArray(GUItype, null);
    }

    private static void gameArrayList(ArrayList<String> aufbau, String ID) {
        aufbau.clear();

        aufbau.add("background"); aufbau.add("background"); aufbau.add("background"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("background"); aufbau.add("background"); aufbau.add("background");
        aufbau.add("background"); aufbau.add("background"); aufbau.add("background"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("background"); aufbau.add("background"); aufbau.add("background");
        aufbau.add("background"); aufbau.add("background"); aufbau.add("background"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("background"); aufbau.add("background"); aufbau.add("background");

        if(ID != null) {

            aufbau.set(3, "blue"); aufbau.set(4, "redField"); aufbau.set(5, "blue");
            aufbau.set(12, "redField"); aufbau.set(13, "blue"); aufbau.set(14, "redField");
            aufbau.set(21, "blue"); aufbau.set(22, "redField"); aufbau.set(23, "blue");
        }
    } private static void mainArrayList(ArrayList<String> aufbau) {
        aufbau.clear();

        aufbau.add("background"); aufbau.add("background"); aufbau.add("background"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("background"); aufbau.add("background"); aufbau.add("background");
        aufbau.add("background"); aufbau.add("background"); aufbau.add("background"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("background"); aufbau.add("background"); aufbau.add("background");
        aufbau.add("background"); aufbau.add("background"); aufbau.add("background"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("empty"); aufbau.add("background"); aufbau.add("background"); aufbau.add("background");
    }

}
