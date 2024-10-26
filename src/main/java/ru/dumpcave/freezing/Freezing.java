package ru.dumpcave.freezing;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dumpcave.freezing.commands.FreezingExecutor;
import ru.dumpcave.freezing.commands.GetFrozen;
import ru.dumpcave.freezing.eventhandler.ProphibitPlayerActions;
import ru.dumpcave.freezing.util.YamlReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

public final class Freezing extends JavaPlugin {
    @Getter
    private String filePath = getDataFolder().getPath();
    private FreezingExecutor freezingExecutor;
    @Override
    public void onEnable() {
        Config.load(getConfig());
        saveConfig();
        freezingExecutor = new FreezingExecutor(this);
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
        getCommand("GetFrozen").setExecutor(new GetFrozen(freezingExecutor));
        getServer().getPluginManager().registerEvents(new ProphibitPlayerActions(this, freezingExecutor), this);
    }
    @Override
    public void onDisable() {
        System.out.println("Массив "+freezingExecutor.getPlayersInFreeze().toString());
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
            FileWriter fw = new FileWriter(finalDir, true);
            PrintWriter pw = new PrintWriter(fw);

            for (UUID uuid : uuidList) {
                pw.println(uuid);
                pw.flush();
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace(); }
    }
}
