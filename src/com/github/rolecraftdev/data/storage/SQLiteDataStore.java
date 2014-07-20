package com.github.rolecraftdev.data.storage;

import java.sql.Connection;

import com.github.rolecraftdev.data.PlayerData;

public final class SQLiteDataStore extends DataStore {

    @Override
    public void intialise() {
        // TODO Auto-generated method stub

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

    @Override
    public String getStoreTypeName() {
        return "SQLite";
    }

}
