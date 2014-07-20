package com.github.rolecraftdev.data.storage;

import java.sql.Connection;

<<<<<<< HEAD
=======
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


>>>>>>> e974701262d7bc31076c506c8e9169981b8ff10d
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

    @Override
    protected Connection getConnection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStoreTypeName() {
        return "SQLite";
    }

}
