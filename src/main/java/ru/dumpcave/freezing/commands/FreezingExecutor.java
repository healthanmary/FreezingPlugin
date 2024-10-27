package ru.dumpcave.freezing.commands;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dumpcave.freezing.Config;
import ru.dumpcave.freezing.Freezing;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FreezingExecutor implements CommandExecutor {
    private Freezing freezing;
    public FreezingExecutor(Freezing freezing) {this.freezing = freezing;

    }
    @Getter
    private Map<UUID, Integer> taskIdMap = new HashMap<>();
    public void setTaskIdMap(UUID uuid, Integer number) { taskIdMap.put(uuid, number); }
    @Getter
    private Map<UUID, Boolean> flyAbilityMap = new HashMap<>();
    @Getter
    private final List<UUID> playersInFreeze = new ArrayList<>();
    public void addToPlayersInFreeze(UUID uuid) {
        playersInFreeze.add(uuid);
    }
    @Getter
    private final String plName = ChatColor.YELLOW + "[" + ChatColor.DARK_GREEN + "Проверка" + ChatColor.YELLOW + "] " + ChatColor.WHITE;
    public void sendTextTitle(Player targetPlayer) {
        String red_color = "#FF0000";
        String blue_color = "#ACE5EE";
        targetPlayer.sendTitle(net.md_5.bungee.api.ChatColor.of(red_color) + "ПРОВЕРКА НА ЧИТЫ", "Следуйте инструкциям в чате", 15, 340, 0);
        targetPlayer.sendMessage("");
        targetPlayer.sendMessage(net.md_5.bungee.api.ChatColor.of(red_color) +"⚠ "+ChatColor.WHITE+"Вы были вызваны на"+net.md_5.bungee.api.ChatColor.of(red_color) + " проверку читов!");
        targetPlayer.sendMessage("Чтобы "+ChatColor.GOLD+"выполнить нижеуказанные действия "+ChatColor.WHITE+"у вас есть 7 минут!");
        targetPlayer.sendMessage("");
        targetPlayer.sendMessage(ChatColor.GOLD +"1."+ChatColor.WHITE+" Зайдите на сайт: "+net.md_5.bungee.api.ChatColor.of(blue_color)+ChatColor.UNDERLINE+"anydesk.com/ru/downloads" + ChatColor.RESET+ChatColor.GRAY+" (Кликабельно)");
        targetPlayer.sendMessage(ChatColor.GOLD+"2."+ChatColor.WHITE+" Нажмите красную кнопку "+net.md_5.bungee.api.ChatColor.of(blue_color)+"\"Скачать\"");
        targetPlayer.sendMessage(ChatColor.GOLD+"3. "+ChatColor.WHITE+"Зайдите в программу");
        targetPlayer.sendMessage(ChatColor.GOLD+"4. "+ChatColor.WHITE+"Сообщите свой логин "+net.md_5.bungee.api.ChatColor.of(blue_color)+"(красные цифры посередине) "+ChatColor.WHITE+"модератору");
        targetPlayer.sendMessage("");
        targetPlayer.sendMessage(net.md_5.bungee.api.ChatColor.of(red_color) +"Отказ / лив / неадекватное поведение / игнор "+ChatColor.WHITE+"- "+net.md_5.bungee.api.ChatColor.of(red_color)+"бан");
        targetPlayer.sendMessage("");
    }
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
            playersInFreeze.add(targetUuid);
            flyAbilityMap.put(targetUuid, targetPlayer.getAllowFlight());

            if (Bukkit.getWorld(Config.Cords.worldname) != null) targetPlayer.teleport(new Location(Bukkit.getWorld(Config.Cords.worldname), Config.Cords.xCord, Config.Cords.yCord, Config.Cords.zCord));
            else sender.sendMessage(plName + ChatColor.DARK_RED + ChatColor.BOLD + "ОШИБКА! " + ChatColor.RED + "Некорректно указано название мира в конфиге! Секция \"worldname\"");

            targetPlayer.setAllowFlight(true);
            targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
            int taskId = Bukkit.getScheduler().runTaskTimer(freezing, () -> {
                sendTextTitle(targetPlayer);

            }, 0L, 350L).getTaskId();
            taskIdMap.put(targetUuid, taskId);
            sender.sendMessage(plName+"Вы вызвали игрока " + ChatColor.RED + targetPlayer.getName());
            return true; }
        else {
            if (!flyAbilityMap.get(targetUuid)) targetPlayer.setAllowFlight(false);
            if (taskIdMap.get(targetUuid) != null) {
                Bukkit.getScheduler().cancelTask(taskIdMap.get(targetUuid));
                taskIdMap.remove(targetUuid);
            }
            else freezing.getLogger().warning(ChatColor.RED + "Невозможно отменить задачу, потому что taskId не найден. UUID игрока: " + targetPlayer);
            freezing.logToFile("["+formattedNow+"] Администратор "+sender.getName() + " разморозил " + targetPlayer.getName(), "logs.yml");
            playersInFreeze.remove(targetUuid);
//            yamlReader.deleteName(targetPlayer.getName());
            targetPlayer.resetTitle();
            sender.sendMessage(plName+"Вы сняли проверку с игрока " + ChatColor.RED + targetPlayer.getName());
            return true;
         }
    }
}
