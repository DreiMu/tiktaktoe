package de.dreimu.minecraft.plugins.apis.guiapi;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GUIItemFunction {

    private String[] functionInfo = {};

    public GUIItemFunction(String[] functionInfo) throws FunctionDeclarationException {
        this.functionInfo = functionInfo;
    }  public GUIItemFunction() {}

    public void openGUI(Player player) {

        //öffnet die GUI
        //functionInfo = {"guiID"}

        player.openInventory(GUI.getInventoryFromID(this.functionInfo[0]));
        
    } public void closeGUI(Player player) {

        //schließt die GUI

        player.getOpenInventory().close();

    } public void setItem(Inventory inv, int slot) {

        //setzt ein Item
        //functionInfo = {"slot","itemID"}

        if(this.functionInfo[0] == "this") {

            inv.setItem(slot, GUIItem.idToGuiItem(this.functionInfo[1]).getItemStack());

        } else {

            inv.setItem(Integer.parseInt(this.functionInfo[0]), GUIItem.idToGuiItem(this.functionInfo[1]).getItemStack());

        }

    } public void setItems(Player player) {

        //setzt alle Items
        //functionInfo = {"guiAufbauID"}

        player.sendMessage("message");

        player.getOpenInventory().getTopInventory().setContents(GUIAufbau.idToGuiAufbau(functionInfo[0]).getItemStackList(GUIAufbau.idToGuiAufbau(functionInfo[0]).getGUIAufbau()));

    }
}
