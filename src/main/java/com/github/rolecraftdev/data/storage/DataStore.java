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
import com.github.rolecraftdev.data.PlayerSettings;
import com.github.rolecraftdev.data.Region2D;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.quest.Quest;
import com.github.rolecraftdev.util.LocationSerializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A DAO used for persistent storage of Rolecraft data, which can be used for
 * CRUD operations.
 *
 * @since 0.0.5
 */
public abstract class DataStore {
    /**
     * The name of the SQL database players table.
     *
     * @since 0.0.5
     */
    public static final String pt = "playertable";
    /**
     * The name of the SQL database metadata table.
     *
     * @since 0.0.5
     */
    public static final String mdt = "metadatatable";
    /**
     * The name of the SQL database guilds table.
     *
     * @since 0.0.5
     */
    public static final String gt = "guildtable";
    // TODO: JavaDoc
    /**
     * @since 0.0.5
     */
    public static final String mde = "metadata";
    /**
     * The Rolecraft database schema version. Used to tell whether we need to
     * update the database schema to a newer version when the data is loaded
     *
     * @since 0.0.5
     */
    public static final String SQLVERSION1 = "1.0";
    // Future versions here

    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
    /**
     * Whether {@link Quest}s are wholly loaded.
     */
    private volatile boolean questsLoaded;

    /**
     * Set the loading status of {@link Quest}s.
     *
     * @param bool the new {@link Quest} loading status
     * @since 0.0.5
     */
    public void setQuestsLoaded(final boolean bool) {
        questsLoaded = bool;
    }

    /**
     * Check whether {@link Quest}s are completely loaded.
     *
     * @return {@code true} if {@link Quest}s are fully loaded
     * @since 0.0.5
     */
    public boolean isQuestsLoaded() {
        return questsLoaded;
    }

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public DataStore(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the associated {@link RolecraftCore} instance.
     *
     * @return the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public RolecraftCore getParent() {
        return plugin;
    }

    /**
     * Initialise this {@link DataStore} implementation.
     *
     * @since 0.0.5
     */
    public abstract void initialise();

    /**
     * Obtain a {@link Connection} used for CRUD operations affecting the
     * backing database.
     *
     * @return a {@link Connection} to the backing database
     * @since 0.0.5
     */
    protected abstract Connection getConnection();

    /**
     * Get the name of the implementation.
     *
     * @return the name of the implementation
     * @since 0.0.5
     */
    public abstract String getStoreTypeName();

    /**
     * Frees / disconnects from the given SQL {@link Connection}.
     *
     * @param connection the SQL {@link Connection} to free
     * @since 0.0.5
     */
    public abstract void freeConnection(Connection connection);

    /**
     * Clear the given {@link PlayerData} in the database as well as in memory.
     * Note that this won't remove the data, but clear all its values except for
     * the owner's {@link UUID} and username. This is typically used for players
     * who are online, as opposed to {@link #clearPlayerData(UUID)}.
     *
     * @param data the {@link PlayerData} to clear
     * @since 0.0.5
     */
    public abstract void clearPlayerData(PlayerData data);

    /**
     * Save all {@link GuildRank}s of the specified {@link Guild} to the used
     * database.
     *
     * @param guild the {@link Guild} to update the {@link GuildRank}s of
     * @since 0.0.5
     */
    public void updateGuildRanks(final Guild guild) {
        final StringBuilder sb = new StringBuilder();
        for (final GuildRank rank : guild.getRanks()) {
            sb.append(rank.serialize());
            sb.append(",");
        }
        final String ranks = sb.substring(0, sb.length() - 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                final Connection connection = getConnection();
                PreparedStatement ps = null;
                final ResultSet rs = null;
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

    /**
     * Close the specified {@link PreparedStatement} and afterwards the given
     * {@link ResultSet}.
     *
     * @param ps the {@link PreparedStatement} that should be closed
     * @param rs the {@link ResultSet} that should be closed
     * @since 0.0.5
     */
    protected void close(final PreparedStatement ps, final ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (final SQLException e) {
            // swallow exception
        }
    }

    /**
     * Save all data a {@link Guild} has to the used database.
     *
     * @param guild the {@link Guild} to save the data of
     * @since 0.0.5
     */
    public void updateGuildData(final Guild guild) {
        final String home = LocationSerializer
                .serialize(guild.getHomeLocation());
        final String name = guild.getName();
        final String leader = guild.getLeader().toString();
        StringBuilder sb = new StringBuilder();
        for (final UUID id : guild.getMembers()) {
            sb.append(id.toString());
            sb.append(",");
        }
        final String members = sb.substring(0, sb.length() - 1);
        sb = new StringBuilder();
        for (final GuildRank rank : guild.getRanks()) {
            sb.append(rank.serialize());
            sb.append(",");
        }
        final String ranks = sb.substring(0, sb.length() - 1);
        final Region2D ghr = guild.getGuildHallRegion();

        final String hall;
        if (ghr == null) {
            hall = null;
        } else {
            hall = ghr.toString();
        }

        final String id = guild.getId().toString();

        new BukkitRunnable() {
            @Override
            public void run() {
                final Connection connection = getConnection();
                PreparedStatement ps = null;
                final ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("UPDATE " + gt + " SET "
                            + "name = ?, "
                            + "leader = ?,"
                            + "members = ?,"
                            + "ranks = ?,"
                            + "home = ?,"
                            + "hall = ?, " +
                            "influence = ?," +
                            "open = ?"
                            + "WHERE uuid = ?");
                    ps.setString(1, name);
                    ps.setString(2, leader);
                    ps.setString(3, members);
                    ps.setString(4, ranks);
                    ps.setString(5, home);
                    ps.setString(6, hall);
                    ps.setString(7, id);
                    ps.setInt(8, guild.getInfluence());
                    ps.setBoolean(9, guild.isOpen());
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
     * Utterly remove a {@link Guild} from the used database.
     *
     * @param guild the {@link Guild} to remove
     * @since 0.0.5
     */
    public void deleteGuild(final Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final Connection connection = getConnection();
                PreparedStatement ps = null;
                final ResultSet rs = null;
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
     * Remove a player's data from the used database. As opposed to
     * {@link #clearPlayerData(PlayerData)} this deletes the player's data from
     * the database and should thus be used for offline players only.
     *
     * @param uuid the {@link UUID} of the player to remove the data of
     * @since 0.0.5
     */
    public void clearPlayerData(final UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final Connection connection = getConnection();
                PreparedStatement ps = null;
                final ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(
                            "DELETE FROM " + pt + " WHERE uuid = ?");
                    ps.setString(1, uuid.toString());
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
     * Upload a {@link Guild} to the database.
     *
     * @param guild the {@link Guild} to save
     * @since 0.0.5
     */
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
                final Connection connection = getConnection();
                PreparedStatement ps = null;
                final ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("INSERT INTO " + gt
                            + " (uuid, name, leader, members, ranks) VALUES (?,?,?,?,?)");
                    ps.setString(1, id);
                    ps.setString(2, name);
                    ps.setString(3, leader);
                    ps.setString(4, leader);
                    ps.setString(5, ranks);
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
     * Load the {@link Guild}s from the database and add them to the given
     * {@link GuildManager}.
     *
     * @param callback the {@link GuildManager} which will hold the loaded
     *        {@link Guild}s
     * @since 0.0.5
     */
    public void loadGuilds(final GuildManager callback) {
        new BukkitRunnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                final Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("SELECT * FROM " + gt);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        final UUID id = UUID.fromString(rs.getString("uuid"));
                        final String name = rs.getString("name");
                        final UUID leader = (!(rs.getString("leader") == null) && !rs
                                .getString("leader").equals("")) ? UUID
                                .fromString(rs.getString("leader")) : null;
                        final Set<UUID> members = new HashSet<UUID>();
                        for (final String s : rs.getString("members")
                                .split(",")) {
                            if (s != null && !s.equals("")) {
                                members.add(UUID.fromString(s));
                            }
                        }
                        final Set<GuildRank> ranks = new HashSet<GuildRank>();
                        for (final String s : rs.getString("ranks").split(",")) {
                            ranks.add(GuildRank.deserialize(s));
                        }
                        final Location home = LocationSerializer
                                .deserialize(rs.getString("home"));
                        final Region2D hall = Region2D
                                .fromString(rs.getString("hall"));
                        final int influence = rs.getInt("influence");
                        final boolean open = rs.getBoolean("open");

                        final Guild guild = new Guild(callback, id, name,
                                leader, members, ranks, home, influence, hall,
                                open);
                        callback.addGuild(guild, true);
                    }

                    callback.completeLoad();
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
     * Add a player to a {@link Guild} in the database.
     *
     * @param uuid the {@link UUID} of the player that will be added to the
     *        {@link Guild}
     * @param guild the {@link Guild} to which the player will be added
     * @since 0.0.5
     */
    public void addPlayerToGuild(final UUID uuid, final Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(
                            "SELECT uuid,members FROM " + gt
                                    + " WHERE uuid = ? ");
                    ps.setString(1, guild.getId().toString());
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        final String members = rs.getString("members").concat(
                                "," + uuid.toString());
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
     * Remove a player from a {@link Guild} in the used database.
     *
     * @param uuid the {@link UUID} of the player to remove
     * @param guild the {@link Guild} from which the player is supposed to be
     *        removed
     * @since 0.0.5
     */
    public void removePlayerFromGuild(final UUID uuid, final Guild guild) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final Connection connection = getConnection();
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
     * Save the given {@link PlayerData} to the database, updating all its
     * values to the ones in memory.
     *
     * @param commit the {@link PlayerData} that should be saved
     * @since 0.0.5
     */
    public void commitPlayerData(final PlayerData commit) {
        commit.setUnloading(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                final Connection connection = getConnection();
                PreparedStatement ps = null;
                final ResultSet rs = null;
                try {
                    ps = connection
                            .prepareStatement("UPDATE "
                                    + pt
                                    + " SET lastname = ?, guild = ?, exp = ?, profession = ?, influence = ? WHERE uuid = ?");
                    ps.setString(1, commit.getPlayerName());
                    // TODO: null check all of this
                    ps.setString(2, commit.getGuild().toString());
                    ps.setFloat(3, commit.getExperience());
                    ps.setString(4, commit.getProfession().toString());
                    ps.setInt(5, commit.getInfluence());
                    ps.setString(6, commit.getPlayerId().toString());
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
     * Save the given {@link PlayerData} through the executor thread. This is
     * required for shutdowns, when scheduling new {@link BukkitTask}s is
     * forbidden.
     *
     * @param commit the {@link PlayerData} that should be saved
     * @since 0.0.5
     */
    public void commitPlayerDataSync(final PlayerData commit) {
        final Connection connection = getConnection();
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            ps = connection
                    .prepareStatement("UPDATE "
                            + pt
                            + " SET lastname = ?, guild = ?, exp = ?, profession = ?, influence = ?, karma = ? WHERE uuid = ?");
            ps.setString(1, commit.getPlayerName());
            ps.setString(2, commit.getGuild().toString());
            ps.setFloat(3, commit.getExperience());
            ps.setString(4, commit.getProfession().toString());
            ps.setInt(5, commit.getInfluence());
            ps.setFloat(6, commit.getKarma());
            ps.setString(7, commit.getPlayerId().toString());
            ps.execute();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps, rs);
            freeConnection(connection);
        }
    }

    /**
     * Retrieve the data of a player from the database, changing the modifiable
     * values in the given {@link PlayerData}.
     *
     * @param callback the {@link PlayerData} which will be modified
     * @param recursive will run the queries on the executor thread when
     *        {@code true}, allowing for recursive look-ups
     * @since 0.0.5
     */
    @SuppressWarnings("deprecation")
    public void requestPlayerData(final PlayerData callback,
            final boolean recursive) {
        if (isQuestsLoaded()) {
            final String uuid = callback.getPlayerId().toString();
            final String name = callback.getPlayerName();
            final float originalSin = plugin.getOriginalSin();
            if (recursive) {
                final Connection connection = getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement("SELECT * FROM " + pt
                            + " WHERE uuid = ?");
                    ps.setString(1, uuid);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                       /* ResultSetMetaData rsmd = rs.getMetaData();
                        Map<UUID, String> questData = new HashMap<UUID, String>();
                        for (int i = 0; i < rsmd.getColumnCount(); i++) {
                            if (rsmd.getColumnName(i).startsWith("quest")) {
                                questData.put(UUID.fromString(
                                                rsmd.getColumnName(i)
                                                        .substring(6)),
                                        rs.getString(i));
                            }
                        }*/
                        callback.initialise(
                                UUID.fromString(rs.getString("guild")),
                                UUID.fromString(rs.getString("profession")),
                                UUID.fromString(
                                        rs.getString("secondprofession")),
                                rs.getInt("influence"), rs.getFloat("exp"),
                                rs.getFloat("karma"), rs.getFloat("mana"),
                                null, PlayerSettings
                                        .fromString(rs.getString("settings")));
                    } else {
                        ps.close();
                        ps = connection.prepareStatement("INSERT INTO " + pt
                                + " (uuid, lastname) VALUES (?,?)");
                        ps.setString(1, uuid);
                        ps.setString(2, name);
                        ps.execute();
                        callback.initialise(null, null, null, 0, 0f,
                                -originalSin,
                                0, null, PlayerSettings.DEFAULT_SETTINGS);
                    }

                } catch (final SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ps, rs);
                    freeConnection(connection);
                }
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        final Connection connection = getConnection();
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        try {
                            ps = connection
                                    .prepareStatement("SELECT * FROM " + pt
                                            + " WHERE uuid = ?");
                            ps.setString(1, uuid);
                            rs = ps.executeQuery();

                            if (rs.next()) {
                                final ResultSetMetaData rsmd = rs.getMetaData();
                                final Map<UUID, String> questData = new HashMap<UUID, String>();
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
                                        UUID.fromString(rs.getString(
                                                "secondprofession")),
                                        rs.getInt("influence"),
                                        rs.getFloat("exp"),
                                        rs.getFloat("karma"),
                                        rs.getFloat("mana"), questData,
                                        PlayerSettings.fromString(
                                                rs.getString("settings")));
                            } else {
                                ps.close();
                                ps = connection
                                        .prepareStatement("INSERT INTO " + pt
                                                + " (uuid, lastname) VALUES (?,?)");
                                ps.setString(1, uuid);
                                ps.setString(2, name);
                                ps.execute();
                                callback.initialise(null, null, null, 0, 0f,
                                        -originalSin, 0, null,
                                        PlayerSettings.DEFAULT_SETTINGS);
                            }

                        } catch (final SQLException ex) {
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
