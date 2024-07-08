package ru.dumpcave.freezing.eventhandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.dumpcave.freezing.commands.FreezingExecutor;

public class MoveFrzPlayer extends FreezingExecutor implements Listener {
    @EventHandler
    private void on(PlayerMoveEvent event) {
        Player playerInEvent = event.getPlayer();
        Boolean isFrozen = playersInFreeze.get(playerInEvent);
        if (isFrozen != null) {
            event.setCancelled(true);
        }
    }
}
