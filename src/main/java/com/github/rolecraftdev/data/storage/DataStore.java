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
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.util.LocationSerializer;

import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class DataStore {

    private final RolecraftCore parent;

    public DataStore(RolecraftCore parent) {
        this.parent = parent;
    }

    public RolecraftCore getParent() {
        return parent;
    }

    public static final String pt = "playertable";
    public static final String mdt = "metadatatable";
    public static final String gt = "guildtable";

    public abstract void intialise();

    public abstract void loadGuilds(final GuildManager callback);

    public abstract void addPlayerToGuild(final UUID uuid, final Guild guild);

    public abstract void removePlayerFromGuild(final UUID uuid,
            final Guild guild);


    protected abstract Connection getConnection();

    protected void close(final PreparedStatement ps, final ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            // swallow exception
        }
    }

    public abstract String getStoreTypeName();

    public void updateGuildData(final Guild guild) {
        final String home = LocationSerializer
                .serialize(guild.getHomeLocation());
        final String name = guild.getName();
        final String leader = guild.getLeader().toString();
        StringBuilder sb = new StringBuilder();
        for (UUID id : guild.getMembers()) {
            sb.append(id.toString());
            sb.append(",");
        }
        final String members = sb.substring(0, sb.length() - 2);
        sb = new StringBuilder();
        for (GuildRank rank : guild.getRanks()) {
            sb.append(rank.serialize());
            sb.append(",");
        }
        final String ranks = sb.substring(0, sb.length() - 2);
        final String hall = guild.getGuildHallRegion().toString();
        final String id = guild.getId().toString();

        new BukkitRunnable() {

            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    // TODO: Method skeleton
                    ps = connection.prepareStatement("UPDATE " + gt + " SET "
                            + "name = ?, "
                            + "leader = ?,"
                            + "members = ?,"
                            + "ranks = ?,"
                            + "home = ?,"
                            + "hall = ? "
                            + "WHERE uuid = ?");
                    ps.setString(1, name);
                    ps.setString(2, leader);
                    ps.setString(3, members);
                    ps.setString(4, ranks);
                    ps.setString(5, home);
                    ps.setString(6, hall);
                    ps.setString(7, id);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                }
            }

        }.runTaskAsynchronously(getParent());
    }

	/**
	 * For use with online players 
	 * 
	 * @param data
	 */
	public abstract void clearPlayerData(PlayerData data) ;

	public void commitPlayerData(final PlayerData commit) {
		
		commit.setUnloading(true);
		
		new BukkitRunnable () {
			@Override
			public void run () {
				Connection connection = getConnection();
		        PreparedStatement ps = null;
		        ResultSet rs = null;
		        try {
		        	ps = connection.prepareStatement("UPDATE " + pt + " SET name = ?, guild = ?, exp = ?, profession = ?, influence = ? WHERE uuid = ?");
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

	public void deleteGuild(final Guild guild) {
		new BukkitRunnable () {
			@Override
			public void run () {
				Connection connection = getConnection();
		        PreparedStatement ps = null;
		        ResultSet rs = null;
		        try {
		        	ps = connection.prepareStatement("DELETE FROM " + gt + " WHERE uuid = ?");
		        	ps.setString(1, guild.getId().toString());
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
	 *
	 * For use with <i>offline players ONLY</i>
     * 
     * @param uuid
     * @see com.github.rolecraftdev.data.storage.DataStore#clearPlayerData(java.util.UUID)
	 */
	public void clearPlayerData(final UUID uuid) {
		new BukkitRunnable () {
			@Override
			public void run () {
				Connection connection = getConnection();
		        PreparedStatement ps = null;
		        ResultSet rs = null;
		        try {
		        	ps = connection.prepareStatement("DELETE FROM " + pt + " WHERE uuid = ?");
		        	ps.setString(1, uuid.toString());
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        } finally {
		            close(ps, rs);
		        }
			}
		}.runTaskAsynchronously(getParent());
		
	}


	public void requestPlayerData(final PlayerData callback) {
		
		final String uuid = callback.getPlayerId().toString();
		final String name = callback.getPlayerName();
		new BukkitRunnable () {
			@SuppressWarnings("deprecation")
			@Override
			public void run () {
				Connection connection = getConnection();
		        PreparedStatement ps = null;
		        ResultSet rs = null;
		        try {
		        	ps = connection.prepareStatement("SELECT * FROM " + pt + " WHERE uuid = ?");
		        	ps.setString(1, uuid);
		        	rs = ps.executeQuery();
		        	
		        	if(rs.next()) {
		        		callback.initialise(
		        				UUID.fromString(rs.getString("guild")),
		        				UUID.fromString(rs.getString("profession")), 
		        				rs.getInt("influence"), rs.getFloat("exp"));
		        	}
		        	else {
		        		ps.close();
		        		ps = connection.prepareStatement("INSERT INTO " + pt + " (uuid, name) VALUES (?,?)");
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

	public void createGuild(final Guild guild) {
		final String id = guild.getId().toString();
		final String name = guild.getName();
		final String leader = guild.getLeader().toString();
		StringBuilder sb = new StringBuilder();
		for (GuildRank rank: guild.getRanks()) {
			sb.append(rank.serialize());
			sb.append(",");
		}
		final String ranks = sb.substring(0,sb.length() -2);
	 	new BukkitRunnable () {
			@Override
			public void run () {
				Connection connection = getConnection();
		        PreparedStatement ps = null;
		        ResultSet rs = null;
		        try {
		        	ps = connection.prepareStatement("INSERT INTO " + gt + "(uuid, name, leader, members, ranks) VALUES (?,?,?,?,?)");
		        	ps.setString(1,id);
		        	ps.setString(2, name);
		        	ps.setString(3, leader);
		        	ps.setString(4, leader);
		        	ps.setString(5, ranks);
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        } finally {
		            close(ps, rs);
		        }
			}
		}.runTaskAsynchronously(getParent());
	
	}
}
