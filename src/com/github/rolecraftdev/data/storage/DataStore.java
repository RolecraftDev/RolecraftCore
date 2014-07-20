package com.github.rolecraftdev.data.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.rolecraftdev.data.PlayerData;

public abstract class DataStore {

    public static final String pt = "playertable";
    public static final String mdt = "metadatatable";

    public abstract void intialise();

    public abstract void requestPlayerData(final PlayerData callback);

    public abstract void commitPlayerData(final PlayerData commit);

    protected abstract Connection getConnection();

    protected void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            // swallow exception
        }
    }

}
