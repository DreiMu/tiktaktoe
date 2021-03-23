package de.dreimu.minecraft.plugins.apis.guiapi;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class GUIItemFunction {
    private String[] getCommandArgs(String command){
        String[] openGUI = {"player"};
        String[] closeGUI = {"player"};
        String[] runFunction = {"player","function"};
        String[] changeItemsFromList = {"player", "list"};
        switch(command) {
            
            case "openGUI":
                return openGUI;
            case "runFunction":
                return runFunction;
            case "closeGUI":
                return closeGUI;
            case "changeItemsFromList":
                return changeItemsFromList;
            default:
                return null;
        }
    }
    public GUIItemFunction(GUIItem item, String function) throws FunctionDeclarationException {
        throw new FunctionDeclarationException("Couldn't Declare the Function: "+function);
    }
}
