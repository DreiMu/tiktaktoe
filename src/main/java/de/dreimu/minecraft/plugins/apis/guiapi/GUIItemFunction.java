package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GUIItemFunction {

    private String[] functionInfo = {};

    public GUIItemFunction(String[] functionInfo) throws FunctionDeclarationException {
        this.functionInfo = functionInfo;
    } 

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

            inv.setItem(slot, GUIItem.uuidToGuiItem(UUID.fromString(functionInfo[1])).getItemStack());

        } else {

            inv.setItem(Integer.parseInt(this.functionInfo[0]), GUIItem.uuidToGuiItem(UUID.fromString(functionInfo[1])).getItemStack());

        }

    } public void setItems(Player player) {
        
                // TODO: remove Debug
                System.out.println("x");

        //setzt alle Items
        //functionInfo = {"guiAufbauID"}

        player.sendMessage("message");

        player.getOpenInventory().getTopInventory().setContents(GUIAufbau.idToGuiAufbau(UUID.fromString(functionInfo[0])).getItemStackArray());

    } public void customFunction(InventoryClickEvent e, Player player, Inventory inv, int slot, GUI plugin) {
        {
            plugin.slotWasClicked(e, player, inv, slot, plugin);
            
        }
    }
}
