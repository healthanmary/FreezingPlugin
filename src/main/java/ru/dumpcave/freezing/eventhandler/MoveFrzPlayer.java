package ru.dumpcave.freezing.eventhandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.dumpcave.freezing.commands.FreezingExecutor;

import java.util.HashMap;

public class MoveFrzPlayer implements Listener {
    private final FreezingExecutor freezingExecutor;
    public MoveFrzPlayer(FreezingExecutor freezingExecutor) {
        this.freezingExecutor = freezingExecutor; }
    @EventHandler
    private void on(PlayerMoveEvent event) {
        Player playerInEvent = event.getPlayer();
        HashMap<Player, Boolean> players = freezingExecutor.getPlayersInFreeze();
        Boolean isFrozen = players.get(playerInEvent);
        if (isFrozen != null) {
            event.setCancelled(true);
        }
    }
}
