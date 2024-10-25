package ru.dumpcave.freezing;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dumpcave.freezing.commands.FreezingExecutor;
import ru.dumpcave.freezing.eventhandler.ProphibitPlayerActions;
import ru.dumpcave.freezing.util.YamlReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class Freezing extends JavaPlugin {
    @Getter
    private String filePath = getDataFolder().getPath();
    @Override
    public void onEnable() {
        Config.load(getConfig());
        saveConfig();
        YamlReader yamlReader = new YamlReader(this);
        FreezingExecutor freezingExecutor = new FreezingExecutor(this);
        try {
            File pluginDirectory = getDataFolder();
            if (!pluginDirectory.exists()) {
                pluginDirectory.mkdirs(); }
            File finalDir = new File(getDataFolder(), "logs.yml");
            if (!finalDir.exists())
                finalDir.createNewFile();
            File fileDir2 = new File(getDataFolder(), "players_in_freeze.yml");
            if (!fileDir2.exists())
                fileDir2.createNewFile();
        }    catch (IOException e) {
            e.printStackTrace(); }
        getCommand("check").setExecutor(freezingExecutor);
        getServer().getPluginManager().registerEvents(new ProphibitPlayerActions(this, freezingExecutor), this);
    }
    @Override
    public void onDisable() {

    }
    public void logToFile(String message, String fileName) {
        try {
            File pluginDirectory = getDataFolder();
            if (!pluginDirectory.exists()) {
                pluginDirectory.mkdirs(); }
            File finalDir = new File(getDataFolder(), fileName);
            if (!finalDir.exists()) {
                finalDir.createNewFile(); }
            FileWriter fw = new FileWriter(finalDir, true);
            PrintWriter pw = new PrintWriter(fw);

            pw.println(message);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace(); }
    }
}
