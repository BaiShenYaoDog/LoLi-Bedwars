package io.github.bedwarsrel.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.bedwarsrel.BedwarsRel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;
import java.util.UUID;

public class ulti {
    public static HikariDataSource dataSource = null;

    public static void initializeDatabaseConnection() {
        Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + BedwarsRel.getInstance().getConfig().getString("database.host") + ":" + BedwarsRel.getInstance().getConfig().getString("database.port") + "/" + BedwarsRel.getInstance().getConfig().getString("database.db") + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
            config.setUsername(BedwarsRel.getInstance().getConfig().getString("database.user"));
            config.setPassword(BedwarsRel.getInstance().getConfig().getString("database.password"));
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
        });
    }

    public static void initializeDatabase() {
        Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + BedwarsRel.getInstance().getConfig().getString("database.table-prefix") + "stats_players (`kills` int(11) NOT NULL DEFAULT '0', `wins` int(11) NOT NULL DEFAULT '0', `score` int(11) NOT NULL DEFAULT '0', `loses` int(11) NOT NULL DEFAULT '0', `name` varchar(255) NOT NULL, `destroyedBeds` int(11) NOT NULL DEFAULT '0', `uuid` varchar(255) NOT NULL, `deaths` int(11) NOT NULL DEFAULT '0', `void` int(11) NOT NULL DEFAULT '0', `finalkills` int(11) NOT NULL DEFAULT '0', `finaldeaths` int(11) NOT NULL DEFAULT '0', PRIMARY KEY (`uuid`))");
                preparedStatement.executeUpdate();
                preparedStatement.close();
                connection.close();
            } catch (Exception e) {
                BedwarsRel.getInstance().getBugsnag().notify(e);
            }
        });
    }

    public static boolean DataExists(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + BedwarsRel.getInstance().getConfig().getString("database.table-prefix") + "stats_players WHERE UUID = ? LIMIT 1");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            boolean 结果 = rs.next();
            rs.close();
            ps.close();
            connection.close();
            return 结果;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void SetData(UUID uuid, String Field, Integer Value) {
        Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
            try {
                if (DataExists(uuid)) {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE " + BedwarsRel.getInstance().getConfig().getString("database.table-prefix") + "stats_players SET " + Field + " = ? WHERE UUID = ?");
                    ps.setDouble(1, Value);
                    ps.setString(2, uuid.toString());
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                }
            } catch (SQLException ignored) {}
        });
    }

    public static void AddData(UUID uuid, String Field, Integer Value) {
        Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
            try {
                if (DataExists(uuid)) {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE " + BedwarsRel.getInstance().getConfig().getString("database.table-prefix") + "stats_players SET " + Field + " = " + Field + "+? WHERE UUID = ?");
                    ps.setDouble(1, Value);
                    ps.setString(2, uuid.toString());
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                    connection = dataSource.getConnection();

                    //=========================================================

                    ps = connection.prepareStatement("UPDATE lobby_stats_players SET " + Field + " = " + Field + "+? WHERE UUID = ?");
                    ps.setDouble(1, Value);
                    ps.setString(2, uuid.toString());
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                }
            } catch (SQLException ignored) {}
        });
    }

    public static int GetData(UUID uuid, String Field) {
        try {
            if (DataExists(uuid)) {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + BedwarsRel.getInstance().getConfig().getString("database.table-prefix") + "stats_players WHERE UUID = ? LIMIT 1");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                int Data = 0;
                if (rs.next()) {
                    Data = rs.getInt(Field);
                }
                rs.close();
                ps.close();
                connection.close();
                return Data;
            }
        } catch (SQLException ignored) {}
        return 0;
    }

    public static void initializeData(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
            try {
                if (!DataExists(player.getUniqueId())) {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO " + BedwarsRel.getInstance().getConfig().getString("database.table-prefix") + "stats_players(uuid, name, deaths, destroyedBeds, kills, loses, score, wins, void, finalkills, finaldeaths) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    ps.setString(1, player.getUniqueId().toString());
                    ps.setString(2, player.getName());
                    ps.setInt(3, 0);
                    ps.setInt(4, 0);
                    ps.setInt(5, 0);
                    ps.setInt(6, 0);
                    ps.setInt(7, 0);
                    ps.setInt(8, 0);
                    ps.setInt(9, 0);
                    ps.setInt(10, 0);
                    ps.setInt(11, 0);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();

                    //=========================================================

                    connection = dataSource.getConnection();
                    ps = connection.prepareStatement("INSERT INTO lobby_stats_players(uuid, name, deaths, destroyedBeds, kills, loses, score, wins, void, finalkills, finaldeaths) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    ps.setString(1, player.getUniqueId().toString());
                    ps.setString(2, player.getName());
                    ps.setInt(3, 0);
                    ps.setInt(4, 0);
                    ps.setInt(5, 0);
                    ps.setInt(6, 0);
                    ps.setInt(7, 0);
                    ps.setInt(8, 0);
                    ps.setInt(9, 0);
                    ps.setInt(10, 0);
                    ps.setInt(11, 0);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                }
            } catch (SQLException ignored) {}
        });
    }
}
