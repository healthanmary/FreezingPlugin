package ru.dumpcave.freezing.db;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class isConnedtedCmd implements CommandExecutor {
    private static final String URL = "jdbc:mysql://localhost:3306/freezingplugin";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            if (!connection.isClosed()) {
                sender.sendMessage("Connected");
            } else sender.sendMessage("Not connected");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
