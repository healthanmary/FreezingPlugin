package ru.dumpcave.freezing.eventhandler;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import ru.dumpcave.freezing.Config;
import ru.dumpcave.freezing.Freezing;
import ru.dumpcave.freezing.commands.FreezingExecutor;

import java.util.Set;
import java.util.UUID;

public class ProphibitPlayerActions implements Listener {
    private Freezing freezing;
    private final FreezingExecutor freezingExecutor;
    public ProphibitPlayerActions(Freezing freezing, FreezingExecutor freezingExecutor) {
        this.freezing = freezing;
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
        Player player = e.getPlayer();
        if (!isFrozen(e.getPlayer().getUniqueId())) return;
        /*Эта строка не работает :( */ player.setAllowFlight(false);
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
                "/msg"
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
        UUID targetUuid = targetPlayer.getUniqueId();

        if (!isFrozen(targetPlayer.getUniqueId())) return;
        targetPlayer.setAllowFlight(true);
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
        if (Bukkit.getWorld(Config.Cords.worldname) != null) targetPlayer.teleport(new Location(Bukkit.getWorld(Config.Cords.worldname), Config.Cords.xCord, Config.Cords.yCord, Config.Cords.zCord));
        else System.out.println(ChatColor.DARK_RED +""+ ChatColor.BOLD + "ОШИБКА! " + ChatColor.RED + "Некорректно указано название мира в конфиге! Секция \"worldname\"");
        int taskId = Bukkit.getScheduler().runTaskTimer(freezing, () -> {
            freezingExecutor.sendTextTitle(targetPlayer);
        }, 0L, 350L).getTaskId();
        freezingExecutor.setTaskIdMap(targetUuid, taskId);
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
