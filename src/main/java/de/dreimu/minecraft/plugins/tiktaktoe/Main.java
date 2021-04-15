package de.dreimu.minecraft.plugins.tiktaktoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.dreimu.minecraft.plugins.apis.guiapi.GUI;
import de.dreimu.minecraft.plugins.apis.guiapi.GUIAufbau;
import de.dreimu.minecraft.plugins.apis.guiapi.GUIItem;
import de.dreimu.minecraft.plugins.apis.guiapi.GUIListener;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

/**
 * Die Main Klasse des TikTakToe Plugins
 */
public class Main extends JavaPlugin {

    /**
     * Der Listener für alle Events (Aktuell nicht verwendet)
     */
    public static class TikTakToeListener implements Listener  {}


    private ArrayList<GUIAufbau> guiAufbauList = new ArrayList<GUIAufbau>();

    private UUID guiUUID = UUID.randomUUID();
    private GUI gui = new GUI(guiUUID, "Main");

    // Speichert die UUID von der gameGUI und dem Hauptmenu ab.
    private UUID mainUUID = UUID.randomUUID();
    private UUID gameGUIUUID = UUID.randomUUID();
    private UUID gameSettingGUIUUID = UUID.randomUUID();

    private ArrayList<UUID> gameUUIDs = new ArrayList<UUID>();
    private ArrayList<TikTakToeGame> tttGames = new ArrayList<TikTakToeGame>();

    // Speichert die UUID jedes GUIItems ab
    private UUID emptyGameGUIItemUUID = UUID.randomUUID();
    private UUID redGameGUIItemUUID = UUID.randomUUID();
    private UUID blueGameGUIItemUUID = UUID.randomUUID();
    private UUID setGameGUIGUIItemUUID = UUID.randomUUID();
    private UUID setPrivateGameGUIGUIItemUUID = UUID.randomUUID();
    private UUID setMainGUIGUIItemUUID = UUID.randomUUID();
    private UUID resetGameFieldUUID = UUID.randomUUID();
    private UUID setGameOeffentlichItemUUID = UUID.randomUUID();
    private UUID setGamePrivatItemUUID = UUID.randomUUID();
    private UUID setEasyKIItemUUID = UUID.randomUUID();
    private UUID setMediumKIItemUUID = UUID.randomUUID();
    private UUID setHardKIItemUUID = UUID.randomUUID();
    private UUID openSettingsItemUUID = UUID.randomUUID();
    private UUID deleteGameItemUUID = UUID.randomUUID();
    private UUID backToGameItemUUID = UUID.randomUUID();

    private StringArgument codeArgument = new StringArgument("code"); 

    private GUIListenerClass guiListenerClass = new GUIListenerClass();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(false);
        this.getLogger().log(Level.INFO, "Loading...");
    }

    private void deleteGame(UUID uuid) {
        try {
            TikTakToeGame.getGameFromID(uuid).delete();
        }catch(Exception e) {e.printStackTrace();}
        try {
            this.tttGames.remove(TikTakToeGame.getGameFromID(uuid));
        }catch(Exception e) {e.printStackTrace();}
        try {
            this.gameUUIDs.remove(uuid);
        }catch(Exception e) {e.printStackTrace();}
    }

    private String[] getCodes(CommandSender sender){

        ArrayList<String> codes = new ArrayList<String>();
        
        try {
            TikTakToeGame.getPrivateGames().get(false).forEach(tttGame -> {codes.add(tttGame.getEasyGameID());});
        } catch (Exception e){}

        String[] codeArray = new String[codes.size()];
        return (String[]) codes.toArray(codeArray);
    }
    
    // Diese Funktion wird zum Initiren des Plugins verwendets
    @Override
    public void onEnable() {

        Command.main = this;

        CommandAPI.onEnable(this);

        // Create our arguments
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new GreedyStringArgument("message"));

        //Create our command
        {
        new CommandAPICommand("tiktaktoe")
            .withAliases("ttt")
            .withSubcommand(
                new CommandAPICommand("resume")
                    .withRequirement(sender -> {
                        Player player = (Player) sender;
                        return gui.inventoryIsGUI(gui.getLatestOpenedInventory(player));
                    })
                    .executes((sender, args) -> {
                        Command.resume(sender);
                    })
            )
            .withSubcommand(
                new CommandAPICommand("help")
                    .executes((sender, args) -> {
                        sender.sendMessage("");
                        sender.sendMessage("Benutzung: ");
                        sender.sendMessage("/tiktaktoe help um dies anzuzeigen");
                        sender.sendMessage("/tiktaktoe join <Code> um dem Spiel einer anderen Person beizutreten");
                        sender.sendMessage("/tiktaktoe new <Öffentlich> [Gegenspieler] um ein eigenes Spiel zu erstellen");
                        sender.sendMessage("/tiktaktoe um die GUI zu öffnen");
                    })
            )
            .withSubcommand(
                new CommandAPICommand("join")
                .withArguments(codeArgument.overrideSuggestions(sender -> this.getCodes(sender)))
                // .withArguments(codeArgument.overrideSuggestions("1","2"))
                .executes((sender, args) -> {
                    Command.join(sender, args);
                }))
            .withSubcommand(
                new CommandAPICommand("new")
                .withRequirement((sender) -> {
                    if(TikTakToeGame.getTTTPlayerFromPlayer((Player) sender) == null){return true;}
                    else{return false;}
                })
                .executes((sender, args) -> {
                    // Neues Spiel wird erstellt
                    Command.newGame(sender, true);
                    // Neues Spiel wird erstellt
                })
            )
            .withSubcommand(
                new CommandAPICommand("new")
                .withArguments(new BooleanArgument("Privat"))
                .withRequirement((sender) -> {
                    if(TikTakToeGame.getTTTPlayerFromPlayer((Player) sender) == null){return true;}
                    else{return false;}
                })
                .executes((sender, args) -> {
                    // Neues Spiel wird erstellt
                    Command.newGame(sender, true);
                    // Neues Spiel wird erstellt
                })
            )
            .withSubcommand(
                new CommandAPICommand("new")
                .withArguments(new BooleanArgument("Privat"))
                .withArguments(new PlayerArgument("Gegenspieler"))
                // Dieser Command lässt sich nur ausführen, wenn man nicht schon in einem Spiel ist
                .withRequirement((sender) -> {
                    if(TikTakToeGame.getTTTPlayerFromPlayer((Player) sender) == null){return true;}
                    else{return false;}
                })
                .executes((sender, args) -> {
                    // Neues Spiel wird erstellt
                    Command.newGame(sender, args);
                    // Neues Spiel wird erstellt
                })
            )
            .withSubcommand(
                new CommandAPICommand("debug")
                .withRequirement(sender -> {
                    return sender instanceof Player;
                })
                    .withPermission(CommandPermission.OP)
                    .withSubcommand(
                        new CommandAPICommand("main")
                            .executes((sender, args) -> {
                                Command.debug(sender, "main");
                            })
                    )
                    .withSubcommand(
                        new CommandAPICommand("game")
                            .executes((sender, args) -> {
                                Command.debug(sender, "game");
                            })
                    )
                    .executes((sender, args) -> {
                        sender.sendMessage("Bitte gebe eine GUI an");
                    })
            )
            .withSubcommand(
                new CommandAPICommand("listall")
                    .withPermission(CommandPermission.OP)
                    .executes((sender, args) -> {
                        Command.listall(sender);
                    })
            )
            .executes((sender, args) -> {
                if(!(sender instanceof Player)) {return;} 
                Command.openMainGUI(sender, args);
            }).register();
        }

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
            String[] openGameGUIItemFunctionList = {"gameGUI"};
            GUI.createGUIItem("customFunction", openGameGUIItemFunctionList, setGameGUIGUIItemUUID, "§r§a§lPlay!", true, Material.BEACON);

            String[] openGameGUIItemFunctionListPrivat = {"privategamegui"};
            GUI.createGUIItem("customFunction", openGameGUIItemFunctionListPrivat, setPrivateGameGUIGUIItemUUID, "§r§a§lSpiele mit Freunden!", true, Material.BEACON);
            String[] openMainGUIItemFunctionList = {this.mainUUID.toString()};
            GUI.createGUIItem("openGUIAufbau", openMainGUIItemFunctionList, setMainGUIGUIItemUUID, "§r§cZurück zum Hauptmenü", false, Material.RED_STAINED_GLASS_PANE);
            
            String[] selectEmptyFieldFunctionList = {"gameItem"};
            GUI.createGUIItem("setItem", selectEmptyFieldFunctionList, blueGameGUIItemUUID, "§rDieses Feld ist schon besetzt", false, Material.BLUE_CONCRETE);
            String[] selectRedFieldFunctionList = {"gameItem"};
            GUI.createGUIItem("customFunction", selectRedFieldFunctionList, emptyGameGUIItemUUID, "§rWähle dieses Feld aus", false, Material.LIGHT_GRAY_CONCRETE);
            String[] selectBlueFieldFunctionList = {"gameItem"};
            GUI.createGUIItem("setItem", selectBlueFieldFunctionList, redGameGUIItemUUID, "§rDieses Feld ist schon besetzt", false, Material.RED_CONCRETE);
            String[] resetGamefieldFunctionList = {"resetGamefield"};
            GUI.createGUIItem("customFunction", resetGamefieldFunctionList, resetGameFieldUUID, "§rSpielfeld leeren", false, Material.RED_CONCRETE);

            String[] oeffentlichGameSettingsFunctionList = {"setGameOeffentlich"};
            GUI.createGUIItem("customFunction", oeffentlichGameSettingsFunctionList, setGameOeffentlichItemUUID, "§rSpiel öffentlich machen", false, Material.LIME_CONCRETE);
            String[] privatGameSettingsFunctionList = {"setGamePrivat"};
            GUI.createGUIItem("customFunction", privatGameSettingsFunctionList, setGamePrivatItemUUID, "§rSpiel privat machen", false, Material.RED_CONCRETE);
            
            String[] easyKIFunctionList = {"setEasyKI"};
            GUI.createGUIItem("customFunction", easyKIFunctionList, setEasyKIItemUUID, "§rEinfache KI (Funktioniert §lnoch§r nicht)", false, Material.LIME_CONCRETE_POWDER);
            String[] mediumKIFunctionList = {"setMediumKI"};
            GUI.createGUIItem("customFunction", mediumKIFunctionList, setMediumKIItemUUID, "§rMittlere KI (Funktioniert §lnoch§r nicht)", false, Material.YELLOW_CONCRETE_POWDER);
            String[] HardKIFunctionList = {"setHardKI"};
            GUI.createGUIItem("customFunction", HardKIFunctionList, setHardKIItemUUID, "§rSchwere KI (Funktioniert §lnoch§r nicht)", false, Material.RED_CONCRETE_POWDER);
            String[] openSettingsFunctionList = {"settingsGUI"};
            GUI.createGUIItem("customFunction", openSettingsFunctionList, openSettingsItemUUID, "§rEinstellungen", false, Material.GRINDSTONE);
            String[] deleteGameFunctionList = {"deleteGame"};
            GUI.createGUIItem("customFunction", deleteGameFunctionList, deleteGameItemUUID, "§rSpiel Löschen", false, Material.BARRIER);
            String[] backToGameGUIItemFunctionList = {"gameGUI"};
            GUI.createGUIItem("customFunction", backToGameGUIItemFunctionList, backToGameItemUUID, "§r§aZurück zum Spiel", false, Material.LIME_STAINED_GLASS_PANE);


            
        }

        // Die Function Lists werden gespeichert
        
        try {
            // Die MainGUI wird initiert
            {
                String[] mainGuiAufbau = {
                    "","","","","","","","","",
                    "","","","",setGameGUIGUIItemUUID.toString(),"","","","",
                    "","","","","","","","",""};
                    guiAufbauList.add(0, new GUIAufbau(mainUUID,"Hauptmenu"));
                    guiAufbauList.get(0).setGUIAufbau(mainGuiAufbau);
                
            }
            // Die GameGUI wird initiert
            {
                String[] gameGuiAufbau = {
                    "","","",emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),"","","",
                    "",resetGameFieldUUID.toString(),"",emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),"",openSettingsItemUUID.toString(),"",
                    "","","",emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),emptyGameGUIItemUUID.toString(),"","",setMainGUIGUIItemUUID.toString()
                };
                guiAufbauList.add(1, new GUIAufbau(gameGUIUUID,"Game"));
                guiAufbauList.get(1).setGUIAufbau(gameGuiAufbau);
            }
            // Die Gamesettingsgui wird initiiert
            {
                String[] gameSettingsGuiAufbau = {
                    "",             "","","","","","","","",
                    "",setGameOeffentlichItemUUID.toString(),"",setEasyKIItemUUID.toString(),setMediumKIItemUUID.toString(),setHardKIItemUUID.toString(),"",deleteGameItemUUID.toString(),"",
                    "",             "","","","","","","",backToGameItemUUID.toString()
                };
                guiAufbauList.add(2, new GUIAufbau(gameSettingGUIUUID,"Spiel bearbeiten"));
                guiAufbauList.get(2).setGUIAufbau(gameSettingsGuiAufbau);
            }
        } catch(Exception e) {}

        // Der Standard GUIAufbau wird auf gameGUI gesetzt
        try {
            gui.setStandardGUIAufbau(gameGUIUUID);
        } catch (Exception e) {}

        gui.addGUIAufbau(mainUUID);
        gui.addGUIAufbau(gameGUIUUID);
        gui.addGUIAufbau(gameSettingGUIUUID);

        gui.initListener(guiListenerClass);

        
        this.getLogger().log(Level.INFO, "This Plugin seems to be loaded succesfully");
        
    }

    public GUI getGUI() {
        return this.gui;
    }

    @Deprecated
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(label.equals("tiktaktoe")) {
            if(args.length > 0) {
                if(args[0].equals("debug")) {
                    if(sender.isOp()||sender.hasPermission("tiktaktoe.admin.debug")) {
                        if(player instanceof Player) {
                            if(args.length == 2 && args[1] == "main") {
                                try {
                                    
                                    gui.openGui(player, mainUUID);
                                    
                                } catch(Exception e) {}
                            } else if(args.length == 2 && args[1] == "game") {
                                try {
                                    
                                    gui.openGui(player, gameGUIUUID);
                                    
                                } catch(Exception e) {}
                            } else if(args.length >= 3 ) {
                                try {
                                    
                                    gui.setGuiAufbau(player, mainUUID);
                                    this.gui.openGui(player,mainUUID);
                                    
                                } catch(Exception e) {}
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
                } else if(args[0].equals("join")) {
                    TikTakToeGame tttGame;
                    InventoryView invView;
                    try {
                        tttGame = TikTakToeGame.getGameFromEasyID(args[1]);
                    } catch(Exception e){
                        player.sendMessage("Das von dir angegebene Spiel existiert nicht");
                        return true;
                    }
                    try {
                        if(tttGame.getPlayers().length < 2) {
                            tttGame.setPlayer2(new TikTakToePlayer(false, player, tttGame.getUUID()));
                            invView = gui.openGui(player, gameGUIUUID);
                            player.openInventory(invView);
                        }
                    } catch (Exception e) {
                        //TODO: handle exception
                    }
                }
            } else {
                gui.openGui(player, mainUUID);
            }
        }
        return true;
    }

    // This code is called before the server stops and after the /reload command
    @Override
    public void onDisable() {
        gui.closeAllInventorys();
    }

    private class GUIListenerClass implements GUIListener {

        @Override 
        public void slotWasClicked(InventoryClickEvent e, Player player, Inventory inv, int slot, GUI plugin) {
            
            String clickedItemUUID = e.getClickedInventory().getItem(slot).getItemMeta().getPersistentDataContainer().get(new NamespacedKey(GUI.plugin, "itemUUID"), PersistentDataType.STRING);
            GUIItem clickedGuiItem = GUIItem.uuidToGuiItem(UUID.fromString(clickedItemUUID));

            String[] ItemFunctionInfos = clickedGuiItem.getItemFunctionInfos();

            int[][] emptyArray =  {{0,0,0},{0,0,0},{0,0,0}};
            TikTakToePlayer tttPlayer;

            Main main = (Main) plugin.getMain();

            switch(ItemFunctionInfos[0].toLowerCase()) {
                case "setgameprivat":
                    try {
                        TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player)).setPrivat();
                    } catch (Exception ex) {}

                    inv.setItem(slot, GUIItem.uuidToGuiItem(setGameOeffentlichItemUUID).getItemStack());
                    break;

                case "setgameoeffentlich":
                    try {
                        UUID uuid = TikTakToeGame.getUUIDFromPlayer(player);
                        
                        TikTakToeGame game = TikTakToeGame.getGameFromID(uuid);
                        
                        game.setOeffentlich();
                    } catch (Exception ex) {}
                    
                    e.getClickedInventory().setItem(slot, GUIItem.uuidToGuiItem(setGamePrivatItemUUID).getItemStack());
                    break;

                case "settingsgui":
                    player.openInventory(gui.getInventory(guiAufbauList.get(2).getUUID(), player));
                    break;

                case "gameitem":

                    TikTakToePlayer tikTakToePlayer = TikTakToeGame.getTTTPlayerFromPlayer(player);
                    TikTakToeGame tikTakToeGame = TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player));


                    GUIItem playerItem;

                    if(tikTakToePlayer.isPlayer1()) {
                        playerItem = GUIItem.uuidToGuiItem(redGameGUIItemUUID);
                    } else {
                        playerItem = GUIItem.uuidToGuiItem(blueGameGUIItemUUID);
                    }


                    if(tikTakToeGame.playerClicked(tikTakToePlayer, slot, playerItem)) {
                        tikTakToeGame.changeActivePlayer();
                    }
                    // gui.openGui(player, mainUUID);
                    try {
                        tikTakToeGame.updateInventory(player, redGameGUIItemUUID, blueGameGUIItemUUID, TikTakToeGame.getUUIDFromPlayer(player), gui);
                    } catch(Exception exception) {}

                    break;

                case "deletegame":
                    main.deleteGame(TikTakToeGame.getUUIDFromPlayer(player));
                    break;
                case "gamegui":
                    player.openInventory(gui.getInventory(gameGUIUUID, player));
                
                    try {
                        tttPlayer = TikTakToeGame.getTTTPlayerFromPlayer(player);
                        if( tttPlayer == null ) {
                            throw new NullPointerException();
                        }
                    } catch (Exception ex) {

                        UUID gameUUID = TikTakToeGame.generateRandomUUID();
                        
                        gameUUIDs.add(gameUUID);
                        tttPlayer = new TikTakToePlayer(true, player, gameUUID);
                        
                        String easyID = TikTakToeGame.generateRandomEasyUUID();

                        TikTakToeGame tttGame = new TikTakToeGame(gameUUID, emptyGameGUIItemUUID, redGameGUIItemUUID, blueGameGUIItemUUID, easyID, false);

                        tttGames.add(tttGame);
                        tttGame.setPlayer1(tttPlayer);
                    }

                    TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player)).updateInventory(player, redGameGUIItemUUID, blueGameGUIItemUUID, TikTakToeGame.getUUIDFromPlayer(player), gui);
                    break;
                case "privategamegui":
                    player.openInventory(gui.getInventory(gameGUIUUID, player));

                    
                
                    try {
                        tttPlayer = TikTakToeGame.getTTTPlayerFromPlayer(player);
                        if( tttPlayer == null ) {
                            throw new NullPointerException();
                        }
                    } catch (Exception ex) {

                        UUID gameUUID = TikTakToeGame.generateRandomUUID();
                        
                        gameUUIDs.add(gameUUID);
                        tttPlayer = new TikTakToePlayer(true, player, gameUUID);
                        
                        String easyID = TikTakToeGame.generateRandomEasyUUID();

                        TikTakToeGame tttGame = new TikTakToeGame(gameUUID, emptyGameGUIItemUUID, redGameGUIItemUUID, blueGameGUIItemUUID, easyID, true);

                        tttGames.add(tttGame);
                        tttGame.setPlayer1(tttPlayer);
                    }

                    TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player)).updateInventory(player, redGameGUIItemUUID, blueGameGUIItemUUID, TikTakToeGame.getUUIDFromPlayer(player), gui);
                    break;
                case "resetgamefield":
                    TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player)).resetGamefield();
                    TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player)).updateInventory(player, redGameGUIItemUUID, blueGameGUIItemUUID, TikTakToeGame.getUUIDFromPlayer(player), gui);
                    TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player)).changeActivePlayer();
                default:
                    break;
            }
        }
    }

    public static class Command {
        public static Main main;

        public static String newGame(CommandSender sender, Boolean privat) {
            CommandAPI.updateRequirements((Player) sender);
            Player player = (Player) sender;

            UUID gameUUID = TikTakToeGame.generateRandomUUID();
            TikTakToePlayer player1 = new TikTakToePlayer(true, player, gameUUID);

            String easyID = TikTakToeGame.generateRandomEasyUUID();

            TikTakToeGame tttGame = new TikTakToeGame(gameUUID, main.emptyGameGUIItemUUID, main.redGameGUIItemUUID, main.blueGameGUIItemUUID, easyID, privat);

            tttGame.setPlayer1(player1);

            main.gui.openGui(player, main.gameGUIUUID);

            return easyID;
        } 

        public static void newGame(CommandSender sender, Object[] args) {
            Boolean privat = (Boolean) args[0];
            String easyID = newGame(sender, privat);
            Player player2 = (Player) args[1];
            
            BaseComponent[] message = new ComponentBuilder(sender.getName()+" hat dich zum TikTakToe Spielen eingeladen, wenn du mitspielen möchtest, gebe ")
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ttt join "+easyID))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Klicke hier, um den Command auszuführen.").create()))
                .append("'/ttt join "+easyID+"'")
                .append(" ein.")
                .create();

            player2.spigot().sendMessage(message);
        }

        

        public static void join(CommandSender sender, Object[] args) {
            Player player = (Player) sender;
            TikTakToeGame tttGame;
            InventoryView invView;
            String easyID = (String) args[0];
            try {
                tttGame = TikTakToeGame.getGameFromEasyID(easyID);
            } catch(Exception e){
                player.sendMessage("Das von dir angegebene Spiel existiert nicht");
                return;
            }
            try {
                if(tttGame.getPlayers().length < 2) {
                    tttGame.setPlayer2(new TikTakToePlayer(false, player, tttGame.getUUID()));
                    invView = main.gui.openGui(player, main.gameGUIUUID);
                    player.openInventory(invView);
                }
            } catch (Exception e) {}
        }

        public static void resume(CommandSender sender) {
            Player player = (Player) sender;
            try {
                main.gui.resume(player);
            } catch(Exception e) {
                player.sendMessage("Du hast kein laufendes Spiel oder irgendetwas ist schiefgelaufen.");
            }
        }

        public static void debug(CommandSender sender, String guiString) {
            Player player = (Player) sender;
            switch(guiString) {
                case "game":
                    try {
                        main.gui.openGui(player, main.gameGUIUUID);
                        
                    } catch(Exception e) {}
                    break;
                default:
                    try {
                        main.gui.openGui(player, main.mainUUID);
                        
                    } catch(Exception e) {}
                    break;
            }
        }

        public static void listall(CommandSender sender) {
            HashMap<Boolean,ArrayList<TikTakToeGame>> privateToGames = TikTakToeGame.getPrivateGames();
            sender.sendMessage("Öffentliche Spiele:");
            sender.sendMessage("---------------");
            try {
            privateToGames.get(false).forEach(tttGame -> {
                try {
                    sender.sendMessage(tttGame.getEasyGameID()+": Spieler 1: "+tttGame.getPlayer1().getPlayer().getName()+", Spieler 2: "+tttGame.getPlayer2().getPlayer().getName());
                } catch(Exception e) {
                    try{
                        sender.sendMessage(tttGame.getEasyGameID()+": Spieler 1: "+tttGame.getPlayer1().getPlayer().getName()+".");
                    } catch(Exception e1) {
                        sender.sendMessage(tttGame.getEasyGameID());
                    }
                }
            });
            } catch(Exception e0){}
            sender.sendMessage("---------------");
            sender.sendMessage("Private Spiele:");
            sender.sendMessage("---------------");
            try {
                privateToGames.get(true).forEach(tttGame -> {
                    try {
                        sender.sendMessage(tttGame.getEasyGameID()+": Spieler 1: "+tttGame.getPlayer1().getPlayer().getName()+", Spieler 2: "+tttGame.getPlayer2().getPlayer().getName());
                    } catch(Exception e) {
                        try{
                            sender.sendMessage(tttGame.getEasyGameID()+": Spieler 1: "+tttGame.getPlayer1().getPlayer().getName()+".");
                        } catch(Exception e1) {
                            sender.sendMessage(tttGame.getEasyGameID());
                        }
                    }
                });
            } catch(Exception e0){}
            sender.sendMessage("---------------");
        }

        public static void openMainGUI(CommandSender sender, Object[] args) {
            Player player = (Player) sender;
            main.gui.openGui(player, main.mainUUID);
        }
    }

    public void setGamePrivat(TikTakToeGame game) {
        TikTakToeGame.setGamePrivat(game);
    }

    public void setOeffentlichPrivat(TikTakToeGame game) {
        TikTakToeGame.setGameOeffentlich(game);
    }

    public UUID getMainUUID() {
        return mainUUID;
    }
}