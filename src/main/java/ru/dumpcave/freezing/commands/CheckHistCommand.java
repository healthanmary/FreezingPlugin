package ru.dumpcave.freezing.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.dumpcave.freezing.Freezing;
import ru.dumpcave.freezing.db.MySqlStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class CheckHistCommand implements CommandExecutor {
    private Freezing freezing;

    public CheckHistCommand(Freezing freezing) {
        this.freezing = freezing;
    }
    private void executeCheckHist(String targetPlayerName, int number, CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(freezing, () -> {
            MySqlStorage mySqlStorage = new MySqlStorage();
            String query = "select * from check_logs2 where lower(target) = lower(?) order by date desc";
            int times = 0;
            try (PreparedStatement ps = mySqlStorage.getConnection().prepareStatement(query)) {
                ps.setString(1, targetPlayerName);
                ResultSet rs = ps.executeQuery();

                boolean alreadySend = false;
                while (rs.next() && times < number) {
                    if (!alreadySend) {
                        sender.sendMessage(ChatColor.YELLOW+"➤ "+ChatColor.WHITE+"История "+ChatColor.GREEN+"проверок"+ChatColor.RED
                                + ChatColor.WHITE+" игрока "+ ChatColor.RED+targetPlayerName + " "+ChatColor.GRAY +"("+ number+")");
                        alreadySend = true;
                    }
                    times++;
                    String fortatedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getTimestamp(5));
                    if (isFreezing(rs)) {
                        sender.sendMessage(ChatColor.GOLD+""+times+". "+ChatColor.LIGHT_PURPLE+"["+fortatedDate+"] "+ChatColor.YELLOW+rs.getString(2)
                                + ChatColor.WHITE + " заморозил " + ChatColor.GREEN + targetPlayerName);
                    } else if (!isFreezing(rs)) {
                        sender.sendMessage(ChatColor.GOLD+""+times+". "+ChatColor.LIGHT_PURPLE+"["+fortatedDate+"] "+ChatColor.YELLOW+rs.getString(2)
                                + ChatColor.WHITE + " разморозил " + ChatColor.GREEN + targetPlayerName);
                    }
                } if (times == 0) sender.sendMessage(ChatColor.YELLOW+"➤ "+ ChatColor.WHITE+"Игрок " + ChatColor.GREEN + targetPlayerName + ChatColor.WHITE+" ни разу" +
                        ChatColor.RED + " не был" + ChatColor.WHITE + " вызван на проверку");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    private boolean isFreezing(ResultSet rs) {
        String str = null;
        try {
            str = rs.getString(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return str.equals("FREEZING");
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
        executeCheckHist(args[0], number, sender);
//        MyMySqlStorage mySqlStorage = new MySqlStorage();
//        String targetPlayerName = args[0];
//        String query = "select * from check_logs2 where lower(target) = lower(?)";
//        int times = 0;
//        try (PreparedStatement ps = mySqlStorage.getConnection().prepareStatement(query)) {
//            ps.setString(1, targetPlayerName);
//            ResultSet rs = ps.executeQuery();
//
//            boolean alreadySend = false;
//            while (rs.next() && times < number) {
//                if (!alreadySend) {
//                    sender.sendMessage(ChatColor.YELLOW+"➤ "+ChatColor.WHITE+"История "+ChatColor.GREEN+"проверок"+ChatColor.RED
//                            + ChatColor.WHITE+" игрока "+ ChatColor.RED+targetPlayerName + " "+ChatColor.GRAY +"("+ number+")");
//                    alreadySend = true;
//                }
//                times++;
//                String fortatedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getTimestamp(5));
//                if (isFreezing(rs)) {
//                    sender.sendMessage(ChatColor.GOLD+""+times+". "+ChatColor.LIGHT_PURPLE+"["+fortatedDate+"] "+ChatColor.YELLOW+rs.getString(2)
//                            + ChatColor.WHITE + " заморозил " + ChatColor.GREEN + targetPlayerName);
//                } else if (!isFreezing(rs)) {
//                    sender.sendMessage(ChatColor.GOLD+""+times+". "+ChatColor.LIGHT_PURPLE+"["+fortatedDate+"] "+ChatColor.YELLOW+rs.getString(2)
//                            + ChatColor.WHITE + " разморозил " + ChatColor.GREEN + targetPlayerName);
//                }
//            } if (times == 0) sender.sendMessage(ChatColor.YELLOW+"➤ "+ ChatColor.WHITE+"Игрок " + ChatColor.GREEN + targetPlayerName + ChatColor.WHITE+" ни разу" +
//                    ChatColor.RED + " не был" + ChatColor.WHITE + " вызван на проверку");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return true;
    }
}
