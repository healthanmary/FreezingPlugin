package ru.dumpcave.freezing.db;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogToDbCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) return true;
        MySqlStorage mySqlStorage = new MySqlStorage();
        // Это не работает, нужно добавить дату в метод
        mySqlStorage.logToDB(sender.getName(), args[0], "FREEZING", );
        return true;
    }
}
