package ru.dumpcave.freezing;

import org.bukkit.plugin.java.JavaPlugin;
import ru.dumpcave.freezing.commands.FreezingExecutor;
import ru.dumpcave.freezing.eventhandler.MoveFrzPlayer;
import ru.dumpcave.freezing.eventhandler.PlayerQuitNotif;

public final class Freezing extends JavaPlugin {

    @Override
    public void onEnable() {
        FreezingExecutor freezingExecutor = new FreezingExecutor();
        getLogger().info("Freezing enable.");
        getCommand("check").setExecutor(freezingExecutor);
        getServer().getPluginManager().registerEvents(new MoveFrzPlayer(freezingExecutor), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitNotif(freezingExecutor), this); }

    @Override
    public void onDisable() {
        getLogger().info("Freezing disable."); }
}
