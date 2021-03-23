package de.dreimu.minecraft.plugins.tiktaktoe;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Variables {
    public static HashMap<Player,Inventory> verteilung = new HashMap<Player,Inventory>();

    public static HashMap<Player,String> PlayerID;

    public static HashMap<String,String> guiTypeToName() {
        HashMap<String,String> guiTypeToName = new HashMap<String,String>();
        guiTypeToName.put("main","Hauptmenü");
        guiTypeToName.put("game","Spiel gegen ");
        return guiTypeToName;
    }
    
    public static HashMap<Integer,String> guiTypeToArray(String GUItype, String ID) {

        HashMap<Integer,String> aufbau = new HashMap<Integer,String>();

        switch (GUItype) {
            case "game":
                aufbau = gameArrayList(ID);
                break;
            case "main":
                aufbau = mainArrayList();
                break;
            default:
                aufbau = mainArrayList();
                break;
            
                
        }

        return aufbau;
    } public static HashMap<Integer,String> guiTypeToArray(String GUItype) {
        return guiTypeToArray(GUItype, null);
    }

    private static HashMap<Integer,String> gameArrayList(String ID) {
        HashMap<Integer,String> aufbau = new HashMap<Integer,String>();

        aufbau.put(0,"background"); aufbau.put(1, "background"); aufbau.put(2, "background"); aufbau.put(3, "empty"); aufbau.put(4, "empty"); aufbau.put(5, "empty"); aufbau.put(6, "background"); aufbau.put(7, "background"); aufbau.put(8, "background");
        aufbau.put(9,"background"); aufbau.put(10, "background"); aufbau.put(11, "background"); aufbau.put(12, "empty"); aufbau.put(13, "empty"); aufbau.put(14, "empty"); aufbau.put(15, "background"); aufbau.put(16, "background"); aufbau.put(17, "background");
        aufbau.put(18,"background"); aufbau.put(19, "background"); aufbau.put(20, "background"); aufbau.put(21, "empty"); aufbau.put(22, "empty"); aufbau.put(23, "empty"); aufbau.put(24, "background"); aufbau.put(25, "background"); aufbau.put(26, "background");

        if(ID != null) {

            aufbau.put(3, "blueField"); aufbau.put(4, "redField"); aufbau.put(5, "blueField");
            aufbau.put(12, "redField"); aufbau.put(13, "blueField"); aufbau.put(14, "redField");
            aufbau.put(21, "blueField"); aufbau.put(22, "redField"); aufbau.put(23, "blueField");
        }

        return aufbau;

    } private static HashMap<Integer,String> mainArrayList() {
        HashMap<Integer,String> aufbau = new HashMap<Integer,String>();

        aufbau.put(0,"background"); aufbau.put(1, "background"); aufbau.put(2, "background"); aufbau.put(3, "empty"); aufbau.put(4, "empty"); aufbau.put(5, "empty"); aufbau.put(6, "background"); aufbau.put(7, "background"); aufbau.put(8, "background");
        aufbau.put(9,"background"); aufbau.put(10, "background"); aufbau.put(11, "background"); aufbau.put(12, "empty"); aufbau.put(13, "empty"); aufbau.put(14, "empty"); aufbau.put(15, "background"); aufbau.put(16, "background"); aufbau.put(17, "background");
        aufbau.put(18,"background"); aufbau.put(19, "background"); aufbau.put(20, "background"); aufbau.put(21, "empty"); aufbau.put(22, "empty"); aufbau.put(23, "empty"); aufbau.put(24, "background"); aufbau.put(25, "background"); aufbau.put(26, "background");

        return aufbau;
    }


}
