package de.dreimu.minecraft.plugins.tiktaktoe;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.dreimu.minecraft.plugins.apis.guiapi.GUI;
import de.dreimu.minecraft.plugins.apis.guiapi.GUIAufbau;
import de.dreimu.minecraft.plugins.apis.guiapi.GUIItem;
import de.dreimu.minecraft.plugins.apis.guiapi.GUIListener;

public class Main extends JavaPlugin {

    public static class TikTakToeListener implements Listener  {

    }

    private ArrayList<GUIAufbau> guiAufbauList = new ArrayList<GUIAufbau>();

    private UUID guiUUID = UUID.randomUUID();
    private GUI gui = new GUI(guiUUID, "Main");

    // Speichert die UUID von der gameGUI und dem Hauptmenu ab.
    private UUID mainUUID = UUID.randomUUID();
    private UUID gameGUIUUID = UUID.randomUUID();

    private ArrayList<UUID> gameUUIDs = new ArrayList<UUID>();
    private ArrayList<TikTakToeGame> tttGames = new ArrayList<TikTakToeGame>();

    // Speichert die UUID jedes GUIItems ab
    private UUID backgroundGUIItemUUID = UUID.randomUUID();
    private UUID emptyGameGUIItemUUID = UUID.randomUUID();
    private UUID redGameGUIItemUUID = UUID.randomUUID();
    private UUID blueGameGUIItemUUID = UUID.randomUUID();
    private UUID setGameGUIGUIItemUUID = UUID.randomUUID();
    private UUID setMainGUIGUIItemUUID = UUID.randomUUID();

    private GUIListenerClass guiListenerClass = new GUIListenerClass();
    
    // Diese Funktion wird zum Initiren des Plugins verwendets
    @Override
    public void onEnable() {
        // Das Plugin der GUI wird gesetzt
        GUI.plugin = this;
        // Der PluginManager wird abgespeichert
        PluginManager pluginManager = getServer().getPluginManager();
        // Der TikTakToeListener wird abgespeichert
        TikTakToeListener tikTakToeListener = new TikTakToeListener();
        // Die Listener werden abgespeichert
        pluginManager.registerEvents(tikTakToeListener, this);
        pluginManager.registerEvents(gui, this);

        // Die GUIItems werden initiert
        {
            String[] backgroundGUIItemFunctionList = {};
            GUI.createGUIItem("null", backgroundGUIItemFunctionList, backgroundGUIItemUUID, "Bitte nicht Entnehmen!", false, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "test", "test2");
            String[] openGameGUIItemFunctionList = {"gameGUI",this.gameGUIUUID.toString()};
            GUI.createGUIItem("customFunction", openGameGUIItemFunctionList, setGameGUIGUIItemUUID, "Play!", true, Material.BEACON , "test", "test2");
            String[] openMainGUIItemFunctionList = {this.gameGUIUUID.toString()};
            GUI.createGUIItem("setItems", openMainGUIItemFunctionList, setMainGUIGUIItemUUID, "Back to main menu", false, Material.RED_STAINED_GLASS_PANE , "test", "test2");
            String[] selectEmptyFieldFunctionList = {"gameItem", this.emptyGameGUIItemUUID.toString()};
            GUI.createGUIItem("setItem", selectEmptyFieldFunctionList, blueGameGUIItemUUID, "Wähle dieses Feld aus", false, Material.BLUE_CONCRETE);
            String[] selectRedFieldFunctionList = {"gameItem", this.redGameGUIItemUUID.toString()};
            GUI.createGUIItem("customFunction", selectRedFieldFunctionList, emptyGameGUIItemUUID, "Wähle dieses Feld aus", false, Material.LIGHT_GRAY_CONCRETE);
            String[] selectBlueFieldFunctionList = {"gameItem", this.blueGameGUIItemUUID.toString()};
            GUI.createGUIItem("setItem", selectBlueFieldFunctionList, redGameGUIItemUUID, "Wähle dieses Feld aus", false, Material.RED_CONCRETE);
        }

        // Die Function Lists werden gespeichert
        
        try {
            // Die MainGUI wird initiert
            {
                String[] mainGuiAufbau = {
                    backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),
                    backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),setGameGUIGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),
                    backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),};
                    guiAufbauList.add(0, new GUIAufbau(mainUUID,"Hauptmenu"));
                    guiAufbauList.get(0).setGUIAufbau(mainGuiAufbau);
            }
            // Die GameGUI wird initiert
            {
                String[] gameGuiAufbau = {
                    backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),
                    backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),
                    backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),backgroundGUIItemUUID.toString(),setMainGUIGUIItemUUID.toString(),};
                guiAufbauList.add(1, new GUIAufbau(gameGUIUUID,"Game"));
                guiAufbauList.get(1).setGUIAufbau(gameGuiAufbau);
            }
        } catch(Exception e) {e.printStackTrace();}

        // Der Standard GUIAufbau wird auf gameGUI gesetzt
        try {
            gui.setStandardGUIAufbau(gameGUIUUID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gui.addGUIAufbau(mainUUID);
        gui.addGUIAufbau(gameGUIUUID);

        gui.initListener(guiListenerClass);
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(label.equals("tiktaktoe")) {
            if(player instanceof Player) {
                this.gui.openGui(player, mainUUID);
            }
            if(args.length > 0) {
                if(args[0].equals("debug")) {
                    if(sender.isOp()||sender.hasPermission("tiktaktoe.admin.debug")) {
                        if(player instanceof Player) {
                            if(args.length == 2 && args[1] == "main") {
                                try {
                                    getLogger().log(Level.INFO, "msg");
                                    
                                    gui.openGui(player, mainUUID);
                                    
                                } catch(Exception e) {
                                    e.printStackTrace();
                                    getLogger().log(Level.WARNING, e.getMessage());
                                }
                            } else if(args.length == 2 && args[1] == "game") {
                                try {
                                    getLogger().log(Level.INFO, "msg");
                                    
                                    gui.openGui(player, gameGUIUUID);
                                    
                                } catch(Exception e) {
                                    e.printStackTrace();
                                    getLogger().log(Level.WARNING, e.getMessage());
                                }
                            } else if(args.length >= 3 ) {
                                try {
                                    
                                    gui.setGuiAufbau(player, mainUUID);
                                    this.gui.openGui(player,mainUUID);
                                    
                                } catch(Exception e) {
                                    e.printStackTrace();
                                    getLogger().log(Level.WARNING, e.getMessage());
                                }
                            }
                        }
                    } else {
                        sender.sendMessage("Du hast keine Rechte diesen Command zu benutzen!");
                    }
                } else if(args[0].equals("resume")) {
                    if(sender instanceof Player) {
                        try {
                            this.gui.resume(player);
                        } catch(Exception e) {
                            player.sendMessage("Du hast kein laufendes Spiel oder irgendetwas ist schiefgelaufen.");
                        }
                    }
                } else if(args[0].equals("debug2")) {
                    
                }
            }
            getLogger().log(Level.INFO, "tiktaktoe cmd ran");
        }
        return true;
    }

    // This code is called before the server stops and after the /reload command
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }

    private class GUIListenerClass implements GUIListener {

        @Override 
        public void slotWasClicked(InventoryClickEvent e, Player player, Inventory inv, int slot, GUI plugin) {

            
            String clickedItemUUID = e.getClickedInventory().getItem(slot).getItemMeta().getPersistentDataContainer().get(new NamespacedKey(GUI.plugin, "itemUUID"), PersistentDataType.STRING);
            GUIItem clickedGuiItem = GUIItem.uuidToGuiItem(UUID.fromString(clickedItemUUID));

            String[] ItemFunctionInfos = clickedGuiItem.getItemFunctionInfos();

            player.sendMessage(ItemFunctionInfos[0]);
            switch(ItemFunctionInfos[0].toLowerCase()) {
                case "gameitem":
                    player.sendMessage("message");
                    TikTakToePlayer tikTakToePlayer = TikTakToeGame.getTTTPlayerFromPlayer(player);
                    TikTakToeGame tikTakToeGame = TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player));

                    GUIItem playerItem;

                    if(tikTakToePlayer.isPlayer1()) {
                        playerItem = GUIItem.uuidToGuiItem(redGameGUIItemUUID);
                    } else {
                        playerItem = GUIItem.uuidToGuiItem(blueGameGUIItemUUID);
                    }

                    ItemStack[][] gameFieldItemStacks = tikTakToeGame.playerClicked(tikTakToePlayer, slot, playerItem);
                    ItemStack[] oldContents = e.getInventory().getContents();
                    ItemStack[] inventoryStacks = {
                    oldContents[0],     oldContents[1],     oldContents[2],     gameFieldItemStacks[0][0],  gameFieldItemStacks[0][1],  gameFieldItemStacks[0][2],  oldContents[6],     oldContents[7],     oldContents[8],
                    oldContents[9],     oldContents[10],    oldContents[11],    gameFieldItemStacks[1][0],  gameFieldItemStacks[1][1],  gameFieldItemStacks[1][2],  oldContents[15],    oldContents[16],    oldContents[17],
                    oldContents[18],    oldContents[19],    oldContents[20],    gameFieldItemStacks[2][0],  gameFieldItemStacks[2][1],  gameFieldItemStacks[2][2],  oldContents[24],    oldContents[25],    oldContents[26]};
                    
                    e.getClickedInventory().setContents(inventoryStacks);
                    Player[] players = TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player)).getPlayers();
                    Player player1 = players[0];
                    Player player2 = players[1];

                    if(gui.inventoryIsGUI(player1.getOpenInventory().getTopInventory())) {
                        player1.getOpenInventory().getTopInventory().setContents(inventoryStacks);
                    }
                    
                    if(gui.inventoryIsGUI(player2.getOpenInventory().getTopInventory())) {
                        player2.getOpenInventory().getTopInventory().setContents(inventoryStacks);
                    }


                    System.out.println(player.getPlayerListName()+" hat auf Slot "+e.getRawSlot()+" geklickt!");
                    player.sendMessage("Du hast auf Slot "+e.getRawSlot()+" geklickt!");
                    break;
                case "gamegui":
                    player.sendMessage("gamegui");
                    TikTakToePlayer tttPlayer;
                    player.sendMessage(gameGUIUUID.toString());
                    gui.setGuiAufbau(player, gameGUIUUID);
                    try {
                        tttPlayer = TikTakToeGame.getTTTPlayerFromPlayer(player);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        UUID gameUUID = UUID.randomUUID();
                        gameUUIDs.add(gameUUID);
                        tttPlayer = new TikTakToePlayer(true, player, gameUUID);
                        TikTakToeGame tttGame = new TikTakToeGame(gameUUID, emptyGameGUIItemUUID, redGameGUIItemUUID, blueGameGUIItemUUID);
                        tttGames.add(tttGame);
                        tttGame.setPlayer1(tttPlayer);
                    }
                default:
                    break;
            }
        }
    }
}