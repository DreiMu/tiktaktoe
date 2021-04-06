package de.dreimu.minecraft.plugins.apis.guiapi;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface GUIListener {
    public void slotWasClicked(InventoryClickEvent e, Player player, Inventory inv, int slot, GUI plugin);
}