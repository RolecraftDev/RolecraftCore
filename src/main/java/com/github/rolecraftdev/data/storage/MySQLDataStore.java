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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

public final class MySQLDataStore extends DataStore {

    public MySQLDataStore(RolecraftCore parent) {
        super(parent);
    }

    private static final String createPlayerTable =
            "CREATE TABLE IF NOT EXISTS " + pt + " ("
                    + "uuid VARCHAR(40) PRIMARY KEY,"
                    + "lastname VARCHAR(16) NOT NULL,"
                    + "FOREIGN KEY (guild) REFERENCES " + gt
                    + "(uuid) ON DELETE SET NULL,"
                    + "exp REAL DEFAULT 0,"
                    + "profession VARCHAR (37) DEFAULT NULL,"
                    + "influence INTEGER DEFAULT 0"
                    + ")";

    private static final String createGuildTable =
            "CREATE TABLE IF NOT EXISTS " + gt + " ("
                    + "uuid VARCHAR(37) PRIMARY KEY ON CONFLICT FAIL,"
                    + "name VARCHAR (50),"
                    + "leader VARCHAR(37) NOT NULL,"
                    + "members MEDIUMTEXT,"
                    + "ranks MEDIUMTEXT,"
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
    public void requestPlayerData(PlayerData callback) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // TODO: Method skeleton

            throw new SQLException();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps, rs);
        }
    }

    @Override
    public void commitPlayerData(PlayerData commit) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // TODO: Method skeleton

            throw new SQLException();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps, rs);
        }

    }

    @Override
    protected Connection getConnection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStoreTypeName() {
        return "MySQL";
    }

    @Override
    public void createGuild(final Guild guild) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // TODO: Method skeleton

            throw new SQLException();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps, rs);
        }

    }

    @Override
    public void loadGuilds(GuildManager callback) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // TODO: Method skeleton

            throw new SQLException();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps, rs);
        }

    }

    @Override
    public void deleteGuild(final Guild guild) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // TODO: Method skeleton

            throw new SQLException();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps, rs);
        }

    }

    @Override
    public void clearPlayerData(UUID uuid) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // TODO: Method skeleton

            throw new SQLException();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps, rs);
        }

    }

    @Override
    public void addPlayerToGuild(final UUID uuid, final Guild guild) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removePlayerFromGuild(final UUID uuid, final Guild guild) {
        // TODO Auto-generated method stub

    }

	@Override
	public void clearPlayerData(final PlayerData data) {
		data.setUnloading(true);
    	new BukkitRunnable () {
    		@SuppressWarnings("deprecation")
			@Override
    		public void run () {
    			Connection connection = getConnection();
    	        PreparedStatement ps = null;
    	        ResultSet rs = null;
    	        try {
	        		ps = connection.prepareStatement("DELETE FROM " + pt + " WHERE uuid = ?");
	        		ps.setString(1, data.getPlayerId().toString());
	        		ps.execute();
	        		ps.close();
	        		ps = connection.prepareStatement("INSERT INTO " + pt + " (uuid, name) VALUES (?,?)");
	        		ps.setString(1,data.getPlayerId().toString());
	        		ps.setString(2, data.getPlayerName());
	        		ps.execute();
	        		data.clear();
    	        } catch (SQLException ex) {
    	            ex.printStackTrace();
    	        } finally {
    	            close(ps, rs);
    	        }
    		}
    	}.runTaskAsynchronously(getParent());
		
	}

}
