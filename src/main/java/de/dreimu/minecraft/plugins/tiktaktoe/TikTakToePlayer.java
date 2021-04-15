package de.dreimu.minecraft.plugins.tiktaktoe;

import java.util.UUID;

import org.bukkit.entity.Player;

import de.dreimu.minecraft.plugins.apis.guiapi.GUI;
import dev.jorel.commandapi.CommandAPI;

/**
 * Der TikTakToePlayer ist der Spieler eines TikTakToeGame
 */
public class TikTakToePlayer {

    /**
     * Speichert, ob der Spieler Spieler 1 oder 2 ist, öffentlich abrufbar mit {@link #isPlayer1()}
     */
    private Boolean player1 = null;
    /**
     * Speichert den Spieler, öffentlich abrufbar mit {@link #getPlayer()}
     */
    private Player player;
    /**
     * Speichert die UUID des Spiels
     */
    private UUID gameID;

    /**
     * @return {@link #player1}
     */
    public Boolean isPlayer1() {
        return(player1);
    }

    /**
     * @return {@link #player}
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return true, if this player is the active one, false if not.
     */
    public Boolean isActivePlayer() {
        // Gibt aus, ob dieser Spieler der Aktive Spieler ist, dh, ob er seinen "Stein" setzen darf, oder nicht.
        return TikTakToeGame.getGameFromID(this.gameID).isActivePlayer(this);
    }

    /**
     * 
     * @param player1 true, if this Player is Player 1, false if not
     * @param player der Player
     * @param gameUUID Die UUID des Spiels
     */
    public TikTakToePlayer(Boolean player1, Player player, UUID gameUUID) {
        this.player1 = player1;
        this.player = player;
        this.gameID = gameUUID;
    }

    /**
     * Deletes the TikTakToeGame
     */
    public void delete() {
        CommandAPI.updateRequirements(this.player);
        this.gameID = null;
    }
}
