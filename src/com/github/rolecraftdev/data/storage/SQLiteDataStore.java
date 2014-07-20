package com.github.rolecraftdev.data.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.rolecraftdev.data.PlayerData;

public final class SQLiteDataStore extends DataStore {
    
    private static final String createPlayerTable = "CREATE TABLE IF NOT EXISTS " + pt + " ("
            + "uuid VARCHAR(40) PRIMARY KEY ON CONFLICT REPLACE,"
            + "lastname VARCHAR(16) NOT NULL ON CONFLICT FAIL" 
            + ")";

    @Override
    public void intialise() {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //TODO: Method skeleton
            
            throw new SQLException();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close (ps,rs);
        }

    }

    @Override
    public void requestPlayerData(PlayerData callback) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //TODO: Method skeleton
            
            throw new SQLException();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close (ps,rs);
        }

    }

    @Override
    public void commitPlayerData(PlayerData commit) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //TODO: Method skeleton
            
            throw new SQLException();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close (ps,rs);
        }

    }

    @Override
    protected Connection getConnection() {
        // TODO Auto-generated method stub
        return null;
    }

}
