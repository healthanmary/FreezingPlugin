package ru.dumpcave.freezing.commands;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dumpcave.freezing.Config;
import ru.dumpcave.freezing.Freezing;
import ru.dumpcave.freezing.util.YamlDelete;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FreezingExecutor implements CommandExecutor {
    private Freezing freezing;
    private YamlDelete yamlReader;
    public FreezingExecutor(Freezing freezing) {
        this.freezing = freezing;
    }
    @Getter
    private final List<UUID> playersInFreeze = new ArrayList<>();
    @Getter
    private final String plName = ChatColor.YELLOW + "[" + ChatColor.DARK_GREEN + "Проверка" + ChatColor.YELLOW + "] " + ChatColor.WHITE;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(plName+"Вы не указали ник игрока.");
            return true; }


        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(plName+"Игрок не найден.");
            return true; }
        UUID targetUuid = targetPlayer.getUniqueId();

        String path = freezing.getFilePath();
        File file = new File(freezing.getDataFolder(), "players_in_freeze.yml");
        String path2 = file.getPath();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        if (!playersInFreeze.contains(targetUuid)) {
            freezing.logToFile("["+formattedNow+"] Администратор "+sender.getName() + " заморозил  " + targetPlayer.getName(), "logs.yml");
            freezing.logToFile(targetPlayer.getName(), "players_in_freeze.yml");
            playersInFreeze.add(targetUuid);

            if (Bukkit.getWorld(Config.Cords.worldname) != null)
                targetPlayer.teleport(new Location(Bukkit.getWorld(Config.Cords.worldname), Config.Cords.xCord, Config.Cords.yCord, Config.Cords.zCord));
            else
                sender.sendMessage(plName + ChatColor.DARK_RED + ChatColor.BOLD + "ОШИБКА! " + ChatColor.RED + "Некорректно указано название мира в конфиге! Секция \"worldname\"");
            targetPlayer.sendTitle(ChatColor.DARK_RED+ "ПРОВЕРКА НА ЧИТЫ", "Следуйте инструкциям в чате.", 15, 600000, 0);
            targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);

            sender.sendMessage(plName+"Вы вызвали игрока " + ChatColor.RED + targetPlayer.getName());
            return true; }
         else {
            freezing.logToFile("["+formattedNow+"] Администратор "+sender.getName() + " разморозил " + targetPlayer.getName(), "logs.yml");
            playersInFreeze.remove(targetUuid);
//            yamlReader.deleteName(targetPlayer.getName());
            targetPlayer.resetTitle();
            sender.sendMessage(plName+"Вы сняли проверку с игрока " + ChatColor.RED + targetPlayer.getName());
            return true;
         }
    }
}
