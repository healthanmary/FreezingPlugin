package ru.dumpcave.freezing.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GetFrozen implements CommandExecutor {
    private FreezingExecutor freezingExecutor;

    public GetFrozen(FreezingExecutor freezingExecutor) {
        this.freezingExecutor = freezingExecutor;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(freezingExecutor.getPlayersInFreeze().toString());
        return true;
    }
}
