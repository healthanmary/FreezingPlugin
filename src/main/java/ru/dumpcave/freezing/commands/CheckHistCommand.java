package ru.dumpcave.freezing.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.dumpcave.freezing.db.MySqlStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckHistCommand implements CommandExecutor {
    private boolean isFreezing(ResultSet rs) {
        String str = null;
        try {
            str = rs.getString(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (str == "FREEZING") return true;
        else return false;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED+"Указано неверное количество аргументов");
            return false;
        }
        int number = 10;
        if (args.length == 2) {
            try {
                number = Integer.parseInt(args[1]);
            } catch (IllegalArgumentException e) {}
        }

        String targetPlayerName = args[0];
        MySqlStorage mySqlStorage = new MySqlStorage();
        int times = 0;
        try {
            Statement statement = mySqlStorage.getConnection().createStatement();
            ResultSet rs = statement.executeQuery("select * from check_logs2");
            sender.sendMessage(ChatColor.YELLOW+"➤ "+ChatColor.WHITE+"История "+ChatColor.GREEN+"проверок"+ChatColor.RED
                    + ChatColor.WHITE+" игрока "+ ChatColor.RED+targetPlayerName + " "+ChatColor.GRAY +"("+ number+")");
            while (rs.next() && times < number) {
                times++;
                if (rs.getString(3).equals(targetPlayerName)) {
                    if (rs.getString(4).equals("FREEZING")) {
                        sender.sendMessage(ChatColor.GOLD + "" + times + ". " + ChatColor.RED + "Некорректно записано значение state");
                    } else if (isFreezing(rs)) {
                        sender.sendMessage(ChatColor.GOLD+""+times+". "+ChatColor.LIGHT_PURPLE+"["+rs.getTimestamp(5)+"] "+ChatColor.YELLOW+rs.getString(2)
                                + ChatColor.WHITE + " заморозил " + ChatColor.GREEN + targetPlayerName);
                    } else if (!isFreezing(rs)) {
                        sender.sendMessage(ChatColor.GOLD+""+times+". "+ChatColor.LIGHT_PURPLE+"["+rs.getTimestamp(5)+"] "+ChatColor.YELLOW+rs.getString(2)
                                + ChatColor.WHITE + " разморозил " + ChatColor.GREEN + targetPlayerName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
