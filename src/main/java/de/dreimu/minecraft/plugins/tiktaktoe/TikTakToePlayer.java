package de.dreimu.minecraft.plugins.tiktaktoe;

import org.bukkit.entity.Player;

public class TikTakToePlayer {

    private Boolean color = null;
    private Player player;
    private String gameID;

    public TikTakToePlayer(Boolean color, Player player, String gameID) {
        this.color = color;
        this.player = player;
        this.gameID = gameID;
    }
}
