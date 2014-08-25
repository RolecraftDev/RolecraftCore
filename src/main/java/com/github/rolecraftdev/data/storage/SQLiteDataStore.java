/*
 * This file is part of RolecraftCore.
 *
 * Copyright (c) 2014 RolecraftDev <http://rolecraftdev.github.com>
 * RolecraftCore is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by-nc-nd/3.0
 *
 * As long as you follow the following terms, you are free to copy and redistribute
 * the material in any medium or format.
 *
 * You must give appropriate credit, provide a link to the license, and indicate
 * whether any changes were made to the material. You may do so in any reasonable
 * manner, but not in any way which suggests the licensor endorses you or your use.
 *
 * You may not use the material for commercial purposes.
 *
 * If you remix, transform, or build upon the material, you may not distribute the
 * modified material.
 *
 * You may not apply legal terms or technological measures that legally restrict
 * others from doing anything the license permits.
 *
 * DISCLAIMER: This is a human-readable summary of (and not a substitute for) the
 * license.
 */
package com.github.rolecraftdev.data.storage;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * The SQLite {@link DataStore} implementation.
 *
 * @since 0.0.5
 */
public final class SQLiteDataStore extends DataStore {
    /**
     * The name of the database used by Rolecraft.
     *
     * @since 0.0.5
     */
    public static final String dbname = "rolecraft";

    /**
     * The query used for creating the player table in the database.
     */
    private static final String createPlayerTable =
            "CREATE TABLE IF NOT EXISTS "
                    + pt
                    + " ("
                    + "uuid VARCHAR PRIMARY KEY ON CONFLICT REPLACE,"
                    + "lastname VARCHAR NOT NULL ON CONFLICT FAIL,"
                    + "guild REFERENCES " + gt + "(uuid) ON DELETE SET NULL,"
                    + "exp REAL DEFAULT 0,"
                    + "profession VARCHAR DEFAULT NULL,"
                    + "secondprofession VARCHAR DEFAULT NULL,"
                    + "influence INTEGER DEFAULT 0,"
                    + "karma REAL DEFAULT 0,"
                    + "mana REAL DEFAULT 0,"
                    + "settings VARCHAR"
                    + ")";
    /**
     * The query used for creating the guild table in the database.
     */
    private static final String createGuildTable = "CREATE TABLE IF NOT EXISTS "
            + gt
            + " ("
            + "uuid VARCHAR PRIMARY KEY ON CONFLICT FAIL,"
            + "name VARCHAR,"
            + "leader VARCHAR,"
            + "members TEXT,"
            + "ranks TEXT,"
            + "home VARCHAR,"
            + "hall VARCHAR,"
            + "influence INTEGER DEFAULT 0," +
            "open BOOLEAN DEFAULT FALSE" + ")";
    /**
     * The query used for creating the metadata table in the database.
     */
    private static final String createMetaTable = "CREATE TABLE IF NOT EXISTS "
            + mdt + " ("
            + "version VARCHAR,"
            + "entry VARCHAR PRIMARY KEY"
            + ")";

    private Connection connection;

    /**
     * Constructor.
     *
     * @param parent the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public SQLiteDataStore(RolecraftCore parent) {
        super(parent);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void initialise() {
        final RolecraftCore parent = getParent();
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(createPlayerTable);
                    ps.execute();
                    ps.close();
                    ps = connection.prepareStatement(createGuildTable);
                    ps.execute();
                    ps.close();
                    ps = connection.prepareStatement(createMetaTable);
                    ps.execute();
                    ps.close();

                    ps = connection.prepareStatement("SELECT version FROM "
                            + mdt + " WHERE entry = ?");
                    ps.setString(1, mde);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        if (!rs.getString("version").equals(
                                DataStore.SQLVERSION1)) {
                            // TODO: in the future versions, add logic to update database
                        }
                    } else {
                        close(ps, rs);
                        Bukkit.getLogger().info("INSERT INTO " + mdt
                                + " (version,entry) "
                                + "VALUES (" + DataStore.SQLVERSION1 + ","
                                + DataStore.mde + ")");
                        ps = connection.prepareStatement(
                                "INSERT INTO " + mdt
                                        + " (version,entry) "
                                        + "VALUES ('" + DataStore.SQLVERSION1
                                        + "','" + DataStore.mde + "')");
                        ps.execute();
                    }

                    parent.setSqlLoaded(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }
        }.runTaskAsynchronously(getParent());
    }

    /**
     * @since 0.0.5
     */
    @Override
    protected Connection getConnection() {
        File dataFile = new File(getParent().getDataFolder(), dbname + ".db");
        if (!dataFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                dataFile.createNewFile();
            } catch (IOException e) {
                getParent().getLogger().log(Level.SEVERE,
                        "File write error: " + dbname + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFile);
            return connection;
        } catch (SQLException ex) {
            getParent().getLogger().log(Level.SEVERE,
                    "SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            getParent().getLogger()
                    .log(Level.SEVERE, "CraftBukkit build error");
        }
        return null;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public String getStoreTypeName() {
        return "SQLite";
    }

    /**
     * @since 0.0.5
     */
    // Do not pull up
    @Override
    public void clearPlayerData(final PlayerData data) {
        data.setUnloading(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("INSERT INTO " + pt
                            + " (uuid, name) VALUES (?,?)");
                    ps.setString(1, data.getPlayerId().toString());
                    ps.setString(2, data.getPlayerName());
                    ps.execute();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }
        }.runTaskAsynchronously(getParent());
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void freeConnection(Connection connection) {
        // Method left intentionally blank
    }

    // @Override
    // public void finalizeQuests(final QuestManager manager) {
    // final Set<UUID> uuids = manager.getIds();
    //
    // new BukkitRunnable() {
    // @Override
    // public void run() {
    // Connection connection = getConnection();
    // PreparedStatement ps = null;
    // ResultSet rs = null;
    // try {
    // ps = connection.prepareStatement(
    // "SELECT * FROM " + pt + " WHERE uuid = NULL");
    // rs = ps.executeQuery();
    // ResultSetMetaData rsmd = ps.getMetaData();
    // LinkedHashSet<UUID> questIds = new LinkedHashSet<UUID>();
    // for (int i = 0; i < rsmd.getColumnCount(); i++) {
    // if (rsmd.getColumnName(i).startsWith("quest")) {
    // questIds.add(UUID.fromString(
    // rsmd.getCatalogName(i).substring(6)));
    // }
    // }
    // ps.close();
    // rs.close();
    //
    // int loadedQuests = 0;
    // Iterator<UUID> iter = uuids.iterator();
    // while (iter.hasNext()) {
    // UUID id = iter.next();
    // if (questIds.contains(id)) {
    // iter.remove();
    // questIds.remove(id);
    // loadedQuests++;
    // }
    // }
    //
    // Bukkit.getLogger()
    // .info("[RolecraftCore] Loaded " + loadedQuests
    // + " quests successfully from SQL");
    //
    // if (uuids.size() != 0) {
    // int addedQuests = 0;
    // iter = uuids.iterator();
    // while (iter.hasNext()) {
    // String name = "quest:" + iter
    // .next(); // quest's columns are quest:<UUID>
    // ps = connection.prepareStatement(
    // "ALTER TABLE " + pt + " ADD COLUMN " + name
    // + " VARCHAR");
    // ps.execute();
    // addedQuests++;
    // ps.close();
    // }
    // Bukkit.getLogger()
    // .info("[RolecraftCore] Added " + addedQuests
    // + " quests to SQL");
    // }
    //
    // if (questIds.size() != 0) {
    // Bukkit.getLogger()
    // .info("[RolecraftCore] Detected " + questIds
    // .size()
    // + " obsolete quests, cannot delete due to database implementation");
    // }
    //
    // } catch (SQLException ex) {
    // ex.printStackTrace();
    // } finally {
    // close(ps, rs);
    // }
    // }
    // }.runTaskAsynchronously(getParent());
    //
    // }
}
