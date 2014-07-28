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
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class SQLiteDataStore extends DataStore {

    public SQLiteDataStore(RolecraftCore parent) {
        super(parent);
    }

    private static final String createPlayerTable =
            "CREATE TABLE IF NOT EXISTS " + pt + " ("
                    + "uuid VARCHAR(37) PRIMARY KEY ON CONFLICT REPLACE,"
                    + "lastname VARCHAR(16) NOT NULL ON CONFLICT FAIL,"
                    + "guild REFERENCES " + gt + "(uuid) ON DELETE SET NULL,"
                    + "exp REAL DEFAULT 0,"
                    + "profession VARCHAR (37) DEFAULT NULL,"
                    + "influence INTEGER DEFAULT 0"
                    + ")";

    private static final String createGuildTable =
            "CREATE TABLE IF NOT EXISTS " + gt + " ("
                    + "uuid VARCHAR(37) PRIMARY KEY ON CONFLICT FAIL,"
                    + "name VARCHAR (50),"
                    + "leader VARCHAR(37) NOT NULL,"
                    + "members TEXT,"
                    + "ranks TEXT,"
                    + "home VARCHAR(150),"
                    + "hall VARCHAR(100)"
                    + ")";

    @Override
    public void intialise() {
    	final RolecraftCore parent = this.getParent();
    	new BukkitRunnable () {
    		@Override
    		public void run () {
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

    @Override
    public void requestPlayerData(final PlayerData callback) {

        final String uuid = callback.getPlayerId().toString();
        final String name = callback.getPlayerName();
        new BukkitRunnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(
                            "SELECT * FROM " + pt + " WHERE uuid = ?");
                    ps.setString(1, uuid);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        callback.initialise(
                                UUID.fromString(rs.getString("guild")),
                                UUID.fromString(rs.getString("profession")),
                                rs.getInt("influence"), rs.getFloat("exp"));
                    } else {
                        ps.close();
                        ps = connection.prepareStatement("INSERT INTO " + pt
                                + " (uuid, name) VALUES (?,?)");
                        ps.setString(1, uuid);
                        ps.setString(2, name);
                        callback.initialise(null, null, 0, 0);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }
        }.runTaskAsynchronously(getParent());
    }

    @Override
    public void commitPlayerData(final PlayerData commit) {

        commit.setUnloading(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("UPDATE " + pt
                            + " SET name = ?, guild = ?, exp = ?, profession = ?, influence = ? WHERE uuid = ?");
                    ps.setString(1, commit.getPlayerName());
                    ps.setString(2, commit.getGuild().toString());
                    ps.setFloat(3, commit.getExp());
                    ps.setString(4, commit.getProfession().toString());
                    ps.setInt(5, commit.getInfluence());
                    ps.setString(6, commit.getPlayerId().toString());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    @Override
    protected Connection getConnection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStoreTypeName() {
        return "SQLite";


    }

    @Override
    public void createGuild(Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    throw new SQLException();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    @Override
    public void loadGuilds(GuildManager callback) {

    	new BukkitRunnable () {
    		@Override
    		public void run () {
    			Connection connection = getConnection();
    	        PreparedStatement ps = null;
    	        ResultSet rs = null;
    	        try {
    	        	throw new SQLException();
    	        } catch (SQLException ex) {
    	            ex.printStackTrace();
    	        } finally {
    	            close(ps, rs);
    	        }
    		}
    	}.runTaskAsynchronously(getParent());

    }

    /**
     * Do not pull up
     * @see com.github.rolecraftdev.data.storage.DataStore#clearPlayerData(com.github.rolecraftdev.data.PlayerData)
     */
    @Override
    public void clearPlayerData(final PlayerData data) {
    	data.setUnloading(true);
    	new BukkitRunnable () {
    		@Override
    		public void run () {
    			Connection connection = getConnection();
    	        PreparedStatement ps = null;
    	        ResultSet rs = null;
    	        try {
	        		ps = connection.prepareStatement("INSERT INTO " + pt + " (uuid, name) VALUES (?,?)");
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

    @Override
    public void clearPlayerData(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    throw new SQLException();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    @Override
    public void addPlayerToGuild(UUID uuid, Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    throw new SQLException();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    @Override
    public void removePlayerFromGuild(UUID uuid, Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    throw new SQLException();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    @Override
    public void updateGuildData(Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    throw new SQLException();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

}
