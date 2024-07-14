package ru.dumpcave.freezing.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class FreezingExecutor implements CommandExecutor {

    private HashMap<Player, Boolean> playersInFreeze = new HashMap<>();
    public HashMap<Player, Boolean> getPlayersInFreeze() {
        return playersInFreeze; }

    private final String plName = ChatColor.YELLOW + "[" + ChatColor.DARK_GREEN + "Проверка" + ChatColor.YELLOW + "] " + ChatColor.WHITE;
    public String getPlName() {
        return plName; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plName+"Команда доступна только игрокам");
            return true; }

        if (args.length < 1) {
            sender.sendMessage(plName+"Вы не указали ник игрока.");
            return true; }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(plName+"Игрок не найден.");
            return true; }

        if (playersInFreeze.get(targetPlayer) == null) {
            playersInFreeze.put(targetPlayer, true);
            targetPlayer.teleport(new Location(Bukkit.getWorld("world"), -51.500, 192, -123.500));
            targetPlayer.sendTitle("§4ПРОВЕРКА НА ЧИТЫ", "Следуйте инструкциям в чате.", 15, 600000, 0);
            targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
            sender.sendMessage(plName+"Вы вызвали игрока " + ChatColor.RED + targetPlayer.getName());
            targetPlayer.sendMessage(plName+"Вы были вызваны на проверку!");
            return true; }

         else {
            playersInFreeze.put(targetPlayer, null);
            targetPlayer.resetTitle();
            sender.sendMessage(plName+"Вы сняли проверку с игрока " + ChatColor.RED + targetPlayer.getName());
            targetPlayer.sendMessage(plName+"С вас была снята проверка.");
            return true;
        }
    }
}
