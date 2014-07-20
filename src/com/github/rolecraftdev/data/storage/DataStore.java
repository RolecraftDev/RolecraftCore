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

<<<<<<< HEAD
    protected void close(final PreparedStatement ps, final ResultSet rs) {
=======
    protected void close(PreparedStatement ps, ResultSet rs) {
>>>>>>> e974701262d7bc31076c506c8e9169981b8ff10d
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            // swallow exception
        }
    }
<<<<<<< HEAD

    public abstract String getStoreTypeName();
=======
>>>>>>> e974701262d7bc31076c506c8e9169981b8ff10d

}
