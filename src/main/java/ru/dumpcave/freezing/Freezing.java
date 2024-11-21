package ru.dumpcave.freezing;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dumpcave.freezing.commands.CheckHistCommand;
import ru.dumpcave.freezing.commands.FreezingExecutor;
import ru.dumpcave.freezing.db.MySqlStorage;
import ru.dumpcave.freezing.eventhandler.ProphibitPlayerActions;

import java.io.*;
import java.util.List;
import java.util.UUID;

public final class Freezing extends JavaPlugin {
    @Getter
    private String filePath = getDataFolder().getPath();
    private FreezingExecutor freezingExecutor;
    private MySqlStorage mySqlStorage;
    @Override
    public void onEnable() {
        Config.load(getConfig());
        saveConfig();

        mySqlStorage = new MySqlStorage();
        freezingExecutor = new FreezingExecutor(this, mySqlStorage);
        conventToArray();
        try {
            File pluginDirectory = getDataFolder();
            if (!pluginDirectory.exists()) {
                pluginDirectory.mkdirs(); }
            File finalDir = new File(getDataFolder(), "logs.yml");
            if (!finalDir.exists())
                finalDir.createNewFile();
            File fileDir2 = new File(getDataFolder(), "frozen_players.yml");
            if (!fileDir2.exists())
                fileDir2.createNewFile();
        }    catch (IOException e) {
            e.printStackTrace(); }
        getCommand("check").setExecutor(freezingExecutor);
        getCommand("checkhist").setExecutor(new CheckHistCommand());
        getServer().getPluginManager().registerEvents(new ProphibitPlayerActions(this, freezingExecutor), this);
        activeActions();
    }
    @Override
    public void onDisable() {
        disableFly();
        logUUidsToFile(freezingExecutor.getPlayersInFreeze());
    }
    public void logToFile(String message, String fileName) {
        try {
            File finalDir = new File(getDataFolder(), fileName);
            FileWriter fw = new FileWriter(finalDir, true);
            PrintWriter pw = new PrintWriter(fw);

            pw.println(message);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace(); }
    }
    public void logUUidsToFile(List<UUID> uuidList) {
        String fileName = "frozen_players.yml";
        try {
            File finalDir = new File(getDataFolder(), fileName);
            FileWriter fw = new FileWriter(finalDir, false);
            PrintWriter pw = new PrintWriter(fw);

            for (UUID uuid : uuidList) {
                pw.println(uuid);
                pw.flush();
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace(); }
    }
    public void conventToArray() {
        String filePath = getDataFolder().getPath() + "\\frozen_players.yml";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    UUID uuid = UUID.fromString(line);
                    freezingExecutor.addToPlayersInFreeze(uuid);
                    System.out.println(line + " UUID занесен в HaspMap");
                } catch (IllegalArgumentException e) {
                    getServer().getLogger().warning("Неверный формат UUID: " + line);
                }
            }
        } catch (IOException e) {
            getServer().getLogger().warning("Ошибка чтения файла frozen_players.yml");
        }
    }
    private void disableFly() {
        for (UUID uuid : freezingExecutor.getPlayersInFreeze()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline())
                player.setAllowFlight(false);
        }
    }
    private void activeActions() {
        for (UUID uuid : freezingExecutor.getPlayersInFreeze()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                int taskId = Bukkit.getScheduler().runTaskTimer(this, () -> {
                    freezingExecutor.sendTextTitle(player);
                }, 0L, 350L).getTaskId();
                freezingExecutor.setTaskIdMap(uuid, taskId);
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
                freezingExecutor.setFlyAbilityMap(uuid, player.getAllowFlight());
                if (Bukkit.getWorld(Config.Cords.worldname) != null) player.teleport(new Location(Bukkit.getWorld(Config.Cords.worldname), Config.Cords.xCord, Config.Cords.yCord, Config.Cords.zCord));
                else System.out.println(ChatColor.DARK_RED +""+ ChatColor.BOLD + "ОШИБКА! " + ChatColor.RED + "Некорректно указано название мира в конфиге! Секция \"worldname\"");
                player.setAllowFlight(true);
            }
        }
    }
}
