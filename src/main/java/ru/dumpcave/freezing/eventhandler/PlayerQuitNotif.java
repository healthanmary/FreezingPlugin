package ru.dumpcave.freezing.eventhandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.dumpcave.freezing.commands.FreezingExecutor;

import java.util.HashMap;

public class PlayerQuitNotif implements Listener {
    public final FreezingExecutor freezingExecutor;
    public PlayerQuitNotif(FreezingExecutor freezingExecutor) {
        this.freezingExecutor = freezingExecutor; }
    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player playerInEvent = e.getPlayer();
        HashMap<Player, Boolean> players = freezingExecutor.getPlayersInFreeze();
        Boolean isFrozen = players.get(playerInEvent);
        if (isFrozen != null) {
            Bukkit.broadcast(freezingExecutor.getPlName()+"Замороженный игрок "+ ChatColor.RED +playerInEvent.getName()+ChatColor.WHITE+" вышел из игры!", "freezing.use");
        }
    }
}
