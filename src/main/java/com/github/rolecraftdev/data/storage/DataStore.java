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
import com.github.rolecraftdev.data.Region2D;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.quest.QuestManager;
import com.github.rolecraftdev.util.LocationSerializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.*;

/**
 * Represents a form of data storage in Rolecraft - implemented in MySQL and
 * SQLite
 */
public abstract class DataStore {
    /**
     * The name of the players table in SQL
     */
    public static final String pt = "playertable";
    /**
     * The name of the metadata table in SQL
     */
    public static final String mdt = "metadatatable";
    /**
     * The name of the guilds table in SQL
     */
    public static final String gt = "guildtable";

    /**
     * The Rolecraft plugin instance
     */
    private final RolecraftCore parent;

    private volatile boolean questsLoaded;

    public void setQuestsLoaded(boolean bool) {
        questsLoaded = bool;
    }

    public boolean isQuestsLoaded() {
        return questsLoaded;
    }

    public DataStore(RolecraftCore parent) {
        this.parent = parent;
    }

    public RolecraftCore getParent() {
        return parent;
    }

    public abstract void intialise();

    protected abstract Connection getConnection();

    public abstract String getStoreTypeName();

    public abstract void finalizeQuests(QuestManager manager);

    public abstract void freeConnection(Connection connection);

    /**
     * For use with online players
     *
     * @param data
     */
    public abstract void clearPlayerData(PlayerData data);

    public void updateGuildRanks(final Guild guild) {
        StringBuilder sb = new StringBuilder();
        for (GuildRank rank : guild.getRanks()) {
            sb.append(rank.serialize());
            sb.append(",");
        }
        final String ranks = sb.substring(0, sb.length() - 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(
                            "UPDATE " + gt + " SET ranks = ? WHERE uuid = ?");
                    ps.setString(1, ranks);
                    ps.setString(2, guild.getId().toString());
                    ps.execute();
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }
        }.runTaskAsynchronously(getParent());
    }

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
        final String members = sb.substring(0, sb.length() - 1);
        sb = new StringBuilder();
        for (GuildRank rank : guild.getRanks()) {
            sb.append(rank.serialize());
            sb.append(",");
        }
        final String ranks = sb.substring(0, sb.length() - 1);
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
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }

        }.runTaskAsynchronously(getParent());
    }

    public void deleteGuild(final Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(
                            "DELETE FROM " + gt + " WHERE uuid = ?");
                    ps.setString(1, guild.getId().toString());
                    ps.execute();
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    /**
     * For use with <i>offline players ONLY</i>
     *
     * @param uuid
     * @see com.github.rolecraftdev.data.storage.DataStore#clearPlayerData(java.util.UUID)
     */
    public void clearPlayerData(final UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(
                            "DELETE FROM " + pt + " WHERE uuid = ?");
                    ps.setString(1, uuid.toString());
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    public void createGuild(final Guild guild) {
        final String id = guild.getId().toString();
        final String name = guild.getName();
        final String leader = guild.getLeader().toString();

        final StringBuilder sb = new StringBuilder();
        for (final GuildRank rank : guild.getRanks()) {
            sb.append(rank.serialize());
            sb.append(",");
        }

        final String ranks = sb.substring(0, sb.length() - 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("INSERT INTO " + gt
                            + " (uuid, name, leader, members, ranks) VALUES (?,?,?,?,?)");
                    ps.setString(1, id);
                    ps.setString(2, name);
                    ps.setString(3, leader);
                    ps.setString(4, leader);
                    ps.setString(5, ranks);
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }
        }.runTaskAsynchronously(getParent());
    }

    public void loadGuilds(final GuildManager callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("SELECT * FROM " + gt);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        UUID id = UUID.fromString(rs.getString("uuid"));
                        String name = rs.getString("name");
                        UUID leader = (!(rs.getString("leader") == null) && !rs
                                .getString("leader").equals("")) ?
                                UUID.fromString(rs.getString("leader")) :
                                null;
                        Set<UUID> members = new HashSet<UUID>();
                        for (String s : rs.getString("members").split(",")) {
                            if (s != null && !s.equals("")) {
                                members.add(UUID.fromString(s));
                            }
                        }
                        Set<GuildRank> ranks = new HashSet<GuildRank>();
                        for (String s : rs.getString("ranks").split(",")) {
                            ranks.add(GuildRank.deserialize(s));
                        }
                        Location home = LocationSerializer
                                .deserialize(rs.getString("home"));
                        Region2D hall = Region2D
                                .fromString(rs.getString("hall"));
                        int influence = rs.getInt("influence");

                        Guild guild = new Guild(callback, id, name, leader,
                                members, ranks, home, influence, hall);
                        callback.addGuild(guild, true);
                    }

                    callback.completeLoad();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    public void addPlayerToGuild(final UUID uuid, final Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(
                            "SELECT uuid,members FROM " + gt
                                    + " WHERE uuid = ? ");
                    ps.setString(1, guild.getId().toString());
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        String members = rs.getString("members");
                        members.concat("," + uuid.toString());
                        ps.close();
                        rs.close();

                        ps = connection.prepareStatement("UPDATE " + gt
                                + " SET members = ? WHERE uuid = ?");
                        ps.setString(1, members);
                        ps.setString(2, guild.getId().toString());
                        ps.execute();
                        ps.close();

                        ps = connection.prepareStatement("UPDATE " + pt
                                + " SET guild = ? WHERE uuid = ?");
                        ps.setString(1, guild.getId().toString());
                        ps.setString(2, uuid.toString());
                        ps.execute();
                    } else {
                        Bukkit.getLogger().warning(
                                "SEVERE ERROR OCCURRED: could not load expected guild from SQL");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }
        }.runTaskAsynchronously(getParent());
    }

    public void removePlayerFromGuild(final UUID uuid, final Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(
                            "SELECT uuid,members,ranks,leader FROM " + gt
                                    + " WHERE uuid = ? ");
                    ps.setString(1, guild.getId().toString());
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        String members = rs.getString("members");
                        String ranks = rs.getString("ranks");
                        String leader = rs.getString("leader");
                        members = members.replaceFirst(uuid.toString(), "");
                        ranks = ranks.replaceFirst(uuid.toString(), "");
                        leader = leader.replaceFirst(uuid.toString(), "");
                        ps.close();
                        rs.close();

                        ps = connection.prepareStatement("UPDATE " + gt
                                + " SET members = ?, ranks = ?, leader = ? WHERE uuid = ?");
                        ps.setString(1, members);
                        ps.setString(2, ranks);
                        ps.setString(3, leader);
                        ps.setString(4, guild.getId().toString());
                        ps.execute();
                        ps.close();

                        ps = connection.prepareStatement("UPDATE " + pt
                                + " SET guild = null WHERE uuid = ?");
                        ps.setString(1, uuid.toString());
                        ps.execute();
                    } else {
                        Bukkit.getLogger().warning(
                                "SEVERE ERROR OCCURRED: could not load expected guild from SQL");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }
        }.runTaskAsynchronously(getParent());

    }

    public void commitPlayerData(final PlayerData commit) {
        commit.setUnloading(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection
                            .prepareStatement("UPDATE "
                                    + pt
                                    + " SET name = ?, guild = ?, exp = ?, profession = ?, influence = ? WHERE uuid = ?");
                    ps.setString(1, commit.getPlayerName());
                    ps.setString(2, commit.getGuild().toString());
                    ps.setFloat(3, commit.getExperience());
                    ps.setString(4, commit.getProfession().toString());
                    ps.setInt(5, commit.getInfluence());
                    ps.setString(6, commit.getPlayerId().toString());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            }
        }.runTaskAsynchronously(getParent());
    }

    /**
     * Used during server shutdown, as you cannot schedule a task during shutdown
     *
     * @param commit
     */
    public void commitPlayerDataSync(final PlayerData commit) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection
                    .prepareStatement("UPDATE "
                            + pt
                            + " SET name = ?, guild = ?, exp = ?, profession = ?, influence = ?, karma = ? WHERE uuid = ?");
            ps.setString(1, commit.getPlayerName());
            ps.setString(2, commit.getGuild().toString());
            ps.setFloat(3, commit.getExperience());
            ps.setString(4, commit.getProfession().toString());
            ps.setInt(5, commit.getInfluence());
            ps.setFloat(6, commit.getKarma());
            ps.setString(7, commit.getPlayerId().toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps, rs);
            freeConnection(connection);
        }
    }

    /**
     * @param callback
     * @param recursive only for recursion, call with false
     */
    @SuppressWarnings("deprecation")
    public void requestPlayerData(final PlayerData callback,
            boolean recursive) {
        if (isQuestsLoaded()) {
            final String uuid = callback.getPlayerId().toString();
            final String name = callback.getPlayerName();
            final float originalSin = parent.getOriginalSin();
            if (recursive) {
                Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("SELECT * FROM " + pt
                            + " WHERE uuid = ?");
                    ps.setString(1, uuid);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        Map<UUID, String> questData = new HashMap<UUID, String>();
                        for (int i = 0; i < rsmd.getColumnCount(); i++) {
                            if (rsmd.getColumnName(i).startsWith("quest")) {
                                questData.put(UUID.fromString(
                                                rsmd.getColumnName(i)
                                                        .substring(6)),
                                        rs.getString(i));
                            }
                        }
                        callback.initialise(
                                UUID.fromString(rs.getString("guild")),
                                UUID.fromString(rs.getString("profession")),
                                rs.getInt("influence"), rs.getFloat("exp"),
                                rs.getFloat("karma"), rs.getFloat("mana"),
                                questData);
                    } else {
                        ps.close();
                        ps = connection.prepareStatement("INSERT INTO " + pt
                                + " (uuid, name) VALUES (?,?)");
                        ps.setString(1, uuid);
                        ps.setString(2, name);
                        callback.initialise(null, null, 0, 0f, -originalSin,
                                0, null);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Connection connection = getConnection();
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        try {
                            ps = connection
                                    .prepareStatement("SELECT * FROM " + pt
                                            + " WHERE uuid = ?");
                            ps.setString(1, uuid);
                            rs = ps.executeQuery();

                            if (rs.next()) {
                                ResultSetMetaData rsmd = rs.getMetaData();
                                Map<UUID, String> questData = new HashMap<UUID, String>();
                                for (int i = 0;
                                     i < rsmd.getColumnCount(); i++) {
                                    if (rsmd.getColumnName(i)
                                            .startsWith("quest")) {
                                        questData.put(UUID.fromString(
                                                        rsmd.getColumnName(i)
                                                                .substring(6)),
                                                rs.getString(i));
                                    }
                                }
                                callback.initialise(
                                        UUID.fromString(rs.getString("guild")),
                                        UUID.fromString(
                                                rs.getString("profession")),
                                        rs.getInt("influence"),
                                        rs.getFloat("exp"),
                                        rs.getFloat("karma"),
                                        rs.getFloat("mana"), questData);
                            } else {
                                ps.close();
                                ps = connection
                                        .prepareStatement("INSERT INTO " + pt
                                                + " (uuid, name) VALUES (?,?)");
                                ps.setString(1, uuid);
                                ps.setString(2, name);
                                callback.initialise(null, null, 0, 0f,
                                        -originalSin, 0, null);
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } finally {
                            close(ps, rs);
                            freeConnection(connection);
                        }
                    }
                }.runTaskAsynchronously(getParent());
            }
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (isQuestsLoaded()) {
                        cancel();
                        requestPlayerData(callback, true);
                    }
                }
            }.runTaskTimerAsynchronously(getParent(), 2, 2);

        }

    }
}
