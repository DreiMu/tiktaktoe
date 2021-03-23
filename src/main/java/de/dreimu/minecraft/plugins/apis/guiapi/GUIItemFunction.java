package de.dreimu.minecraft.plugins.apis.guiapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIItemFunction {

    private String[] functionInfo;

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

    } public void setItem(Inventory inv) {

        //setzt ein Item
        //functionInfo = {"slot","itemID"}

        inv.setItem(Integer.parseInt(this.functionInfo[0]), GUIItem.idToGuiItem(this.functionInfo[1]).getItemStack());

    } public void setItems(Player player) {

        //setzt alle Items
        //functionInfo = {"guiAufbauID"}

        player.getOpenInventory().getTopInventory().setContents(GUIAufbau.idToGuiAufbau(functionInfo[0]).getItemStackList());

    }
}
