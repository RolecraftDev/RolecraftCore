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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.scheduler.BukkitRunnable;

public final class MySQLDataStore extends DataStore {
    
    private final String password;
    private final String user;
    private final int port;
    private final String uri;
    private final String databaseName;
    
    //                               minutes
    private static final int killTime = 5 
            * 60000;
    
    //                        connection      in use? last use
    private ConcurrentHashMap<Connection,Entry<Boolean,Long>> connections;
    
    private static final String createPlayerTable = "CREATE TABLE IF NOT EXISTS "+ pt + " ("
            + "uuid VARCHAR(40) PRIMARY KEY,"
            + "lastname VARCHAR(16) NOT NULL,"
            + "FOREIGN KEY (guild) REFERENCES "+ gt + "(uuid) ON DELETE SET NULL,"
            + "exp REAL DEFAULT 0,"
            + "profession VARCHAR (37) DEFAULT NULL,"
            + "influence INTEGER DEFAULT 0" + ")";

    private static final String createGuildTable = "CREATE TABLE IF NOT EXISTS "+ gt+ " ("
            + "uuid VARCHAR(37) PRIMARY KEY ON CONFLICT FAIL,"
            + "name VARCHAR (50),"
            + "leader VARCHAR(37),"
            + "members MEDIUMTEXT,"
            + "ranks MEDIUMTEXT,"
            + "home VARCHAR(150)," 
            + "hall VARCHAR(100),"
            + "influence INTEGER DEFAULT 0" + ")";

    public MySQLDataStore(RolecraftCore parent) {
        super(parent);
        
        password = parent.getConfig().getString("mysql.password");
        user = parent.getConfig().getString("mysql.username");
        uri = parent.getConfig().getString("mysql.address");
        port = parent.getConfig().getInt("mysql.port", 3306);
        databaseName = parent.getConfig().getString("mysql.databasename");
        
        connections = new ConcurrentHashMap<Connection,Entry<Boolean,Long>> ();
        
        new BukkitRunnable () {
            @Override
            public void run () {
                Iterator<Entry<Connection, Entry<Boolean, Long>>> iter = connections.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<Connection,Entry<Boolean,Long>> conn = iter.next();
                    try {
                        if(conn.getKey() == null || conn.getKey().isClosed()) {
                            // if in use, reset timer
                            if(conn.getValue().getKey() == true) {
                                conn.getValue().setValue(System.currentTimeMillis());
                            }
                            // else check for age and close and remove/keepalive
                            else {
                                if(conn.getValue().getValue() + killTime < System.currentTimeMillis()) {
                                    if (connections.size() > 0){
                                        conn.getKey().close();
                                        iter.remove();
                                    } else {
                                        // keepalive
                                        conn.getKey().prepareCall("SELECT 1").execute();
                                    }
                                } else {
                                    // keepalive
                                    conn.getKey().prepareCall("SELECT 1").execute();
                                }
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskTimerAsynchronously(getParent(), 20 * 20, 20 * 20);
    }

    @Override
    public void intialise() {
        final RolecraftCore parent = this.getParent();
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
     * DO NOT PULL UP
     * 
     * @see com.github.rolecraftdev.data.storage.DataStore#clearPlayerData(com.github.rolecraftdev.data.PlayerData)
     */
    @Override
    public void clearPlayerData(final PlayerData data) {
        data.setUnloading(true);
        new BukkitRunnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("DELETE FROM " + pt
                            + " WHERE uuid = ?");
                    ps.setString(1, data.getPlayerId().toString());
                    ps.execute();
                    ps.close();
                    ps = connection.prepareStatement("INSERT INTO " + pt
                            + " (uuid, name) VALUES (?,?)");
                    ps.setString(1, data.getPlayerId().toString());
                    ps.setString(2, data.getPlayerName());
                    ps.execute();
                    data.clear();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    @Override
    protected Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Iterator<Entry<Connection, Entry<Boolean, Long>>> iter = connections.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Connection,Entry<Boolean,Long>> conn = iter.next();
                if(!conn.getValue().getKey()) {
                    conn.setValue(new SimpleEntry<Boolean,Long>(true, System.currentTimeMillis()));
                    return conn.getKey();
                }
            }
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + uri + ":" + port + "/" + databaseName + "?user=" + user + "&password=" + password);
            connections.put(conn, new SimpleEntry<Boolean,Long>(true,System.currentTimeMillis()));
            return conn;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getStoreTypeName() {
        return "MySQL";
    }

    @Override
    public void freeConnection(Connection connection) {
        connections.put(connection, new SimpleEntry<Boolean,Long>(false,System.currentTimeMillis()));
    }

}
