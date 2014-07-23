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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

public final class SQLiteDataStore extends DataStore {

    public SQLiteDataStore(RolecraftCore parent) {
        super(parent);
    }

    private static final String createPlayerTable = "CREATE TABLE IF NOT EXISTS "  + pt  + " ("
            + "uuid VARCHAR(37) PRIMARY KEY ON CONFLICT REPLACE,"
            + "lastname VARCHAR(16) NOT NULL ON CONFLICT FAIL,"
            + "guild REFERENCES " + gt + "(uuid) ON DELETE SET NULL"
            + ")";
    
    private static final String createGuildTable = "CREATE TABLE IF NOT EXISTS " + gt + " ("
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
        return "SQLite";
    }

    @Override
    public void createGuild(Guild guild) {
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
    public void deleteGuild(Guild guild) {
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
    public void addPlayerToGuild(UUID uuid, Guild guild) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removePlayerFromGuild(UUID uuid, Guild guild) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateGuildData(Guild guild) {
        // TODO Auto-generated method stub
        
    }

}
