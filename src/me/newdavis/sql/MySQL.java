package me.newdavis.sql;

import me.newdavis.file.SettingsFile;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {

    private static Connection connection;
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public MySQL(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public boolean connect() {
        try {
            if(connection != null && !connection.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=" + SettingsFile.getMySQLUseSSL(), user, password);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        connect();
        return connection;
    }

    public void createTables() {
        try {
            //Role
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS role (ROLE VARCHAR(40),PREFIX VARCHAR(40),SUFFIX VARCHAR(40),DEFAULT_ROLE INT(1))");
            ps.executeUpdate();

            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS role_permissions (ROLE VARCHAR(40),PERMISSION VARCHAR(40))");
            ps.executeUpdate();

            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS role_inheritance (ROLE VARCHAR(40),INHERITANCE VARCHAR(40))");
            ps.executeUpdate();

            //Player
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player (UUID VARCHAR(40),ROLE VARCHAR(40),PREFIX VARCHAR(40),SUFFIX VARCHAR(40))");
            ps.executeUpdate();

            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_permissions (UUID VARCHAR(40),PERMISSION VARCHAR(40))");
            ps.executeUpdate();
            disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
