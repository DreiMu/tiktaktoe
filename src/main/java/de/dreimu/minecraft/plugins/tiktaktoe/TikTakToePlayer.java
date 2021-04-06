package de.dreimu.minecraft.plugins.tiktaktoe;

import java.util.UUID;

import org.bukkit.entity.Player;

public class TikTakToePlayer {

    private Boolean player1 = null;
    private Player player;
    private UUID gameID;

    public Boolean isPlayer1() {
        return(player1);
    }

    public Player getPlayer() {
        return player;
    }

    public Boolean isActivePlayer() {
        // Gibt aus, ob dieser Spieler der Aktive Spieler ist, dh, ob er seinen "Stein" setzen darf, oder nicht.
        return TikTakToeGame.getGameFromID(this.gameID).isActivePlayer(this);
    }

    public TikTakToePlayer(Boolean player1, Player player, UUID gameUUID) {
        this.player1 = player1;
        this.player = player;
        this.gameID = gameUUID;
    }
}
