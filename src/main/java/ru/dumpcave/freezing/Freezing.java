package ru.dumpcave.freezing;

import org.bukkit.plugin.java.JavaPlugin;
import ru.dumpcave.freezing.commands.FreezingExecutor;
import ru.dumpcave.freezing.eventhandler.MoveFrzPlayer;
import ru.dumpcave.freezing.eventhandler.PlayerQuitNotif;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class Freezing extends JavaPlugin {
    @Override
    public void onEnable() {
        FreezingExecutor freezingExecutor = new FreezingExecutor(this);

        try {
            File pluginDirectory = getDataFolder();
            if (!pluginDirectory.exists()) {
                pluginDirectory.mkdirs(); }
            File finalDir = new File(getDataFolder(), "logs.yml");
            if (!finalDir.exists()) {
                finalDir.createNewFile(); }
        }    catch (IOException e) {
            e.printStackTrace(); }
        getLogger().info("Freezing enable.");
        getCommand("check").setExecutor(freezingExecutor);
        getServer().getPluginManager().registerEvents(new MoveFrzPlayer(freezingExecutor), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitNotif(freezingExecutor), this); }
    @Override
    public void onDisable() {
        getLogger().info("Freezing disable."); }

    public void logToFile(String message) {
        try {
            File pluginDirectory = getDataFolder();
            if (!pluginDirectory.exists()) {
                pluginDirectory.mkdirs(); }
            File finalDir = new File(getDataFolder(), "logs.yml");
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
