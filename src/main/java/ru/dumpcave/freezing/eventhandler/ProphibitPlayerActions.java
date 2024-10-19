package ru.dumpcave.freezing.eventhandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import ru.dumpcave.freezing.commands.FreezingExecutor;

import java.util.Set;
import java.util.UUID;

public class ProphibitPlayerActions implements Listener {
    private final FreezingExecutor freezingExecutor;
    public ProphibitPlayerActions(FreezingExecutor freezingExecutor) {
        this.freezingExecutor = freezingExecutor;
    }
    public boolean isFrozen(UUID uuid) {
        return freezingExecutor.getPlayersInFreeze().contains(uuid);
    }
    @EventHandler
    private void onPlayerMoveEvent(PlayerMoveEvent e) {
        if (!isFrozen(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }
    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        UUID uuid = p.getUniqueId();
        if (!isFrozen(uuid)) return;
        e.setCancelled(true);
    }
    @EventHandler
    private void onPlayerQuitEvent(PlayerQuitEvent e) {
        if (!isFrozen(e.getPlayer().getUniqueId())) return;
        Bukkit.broadcast(freezingExecutor.getPlName()+"Замороженный игрок "+ ChatColor.RED +
                e.getPlayer().getName()  + ChatColor.WHITE+" вышел из игры!", "freezing.use");

    }
    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        Player targetPlayer = e.getPlayer();
        String command = e.getMessage();
        if (!isFrozen(targetPlayer.getUniqueId())) return;
        Set<String> allowedCommands = Set.of(
                "/r",
                "/m",
                "/msg",
                "/check"
        );
        String[] parts = command.split(" ");
        if (!allowedCommands.contains(parts[0])) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
        Player targetPlayer = e.getPlayer();
        if (isFrozen(targetPlayer.getUniqueId())) e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player targetPlayer = e.getPlayer();
        if (!isFrozen(targetPlayer.getUniqueId())) return;

        targetPlayer.sendTitle(ChatColor.DARK_RED+ "ПРОВЕРКА НА ЧИТЫ", "Следуйте инструкциям в чате.",
                15, 600000, 0);
    }
    @EventHandler
    public void onPlayerAttemptPickupItemEvent(PlayerAttemptPickupItemEvent e) {
        Player targetPlayer = e.getPlayer();
        if (isFrozen(targetPlayer.getUniqueId())) e.setCancelled(true);
    }
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Player targetPlayer = e.getPlayer();
        if (isFrozen(targetPlayer.getUniqueId())) e.setCancelled(true);
    }
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player targetPlayer = (Player) e.getEntity();
        if (isFrozen(targetPlayer.getUniqueId())) e.setCancelled(true);
    }
    @EventHandler
    public void onEntityDamageEvent2(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player targetPlayer)) return;
        if (isFrozen(targetPlayer.getUniqueId())) e.setCancelled(true);
    }
}
