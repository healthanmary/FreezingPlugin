package ru.dumpcave.freezing.db;

import lombok.Getter;

import java.sql.*;
import java.util.Date;

public class MySqlStorage {
    private static final String URL = "jdbc:mysql://localhost:3306/freezingplugin";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";
    public void logToDB(String moder, String target, String type, Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement ps = connection.prepareStatement("insert into check_logs2(moder, target, type, date) values (?, ?, ?, ?)");) {
            ps.setString(1, moder);
            ps.setString(2, target);
            ps.setString(3, type);
            ps.setDate(4, sqlDate);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }
}
