package com.github.rolecraftdev.data.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.rolecraftdev.data.PlayerData;

public final class MySQLDataStore extends DataStore {

    @Override
    public void intialise() {
        Connection connection = getConnection();

    }

    @Override
    public void requestPlayerData(PlayerData callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void commitPlayerData(PlayerData commit) {
        // TODO Auto-generated method stub

    }

    @Override
    protected Connection getConnection() {
        // TODO Auto-generated method stub
        return null;
    }

}
