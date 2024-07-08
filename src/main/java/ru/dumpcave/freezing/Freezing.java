package ru.dumpcave.freezing;

import org.bukkit.plugin.java.JavaPlugin;
import ru.dumpcave.freezing.commands.FreezingExecutor;
import ru.dumpcave.freezing.eventhandler.MoveFrzPlayer;

public final class Freezing extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Freezing enable.");
        getCommand("check").setExecutor(new FreezingExecutor());
        getServer().getPluginManager().registerEvents(new MoveFrzPlayer(), this); }

    @Override
    public void onDisable() {
        getLogger().info("Freezing disable."); }
}
