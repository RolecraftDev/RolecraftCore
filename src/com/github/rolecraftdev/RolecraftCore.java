package com.github.rolecraftdev;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.storage.DataStore;
import com.github.rolecraftdev.data.storage.mysql.MySQLDataStore;
import com.github.rolecraftdev.data.storage.sqlite.SQLiteDataStore;

public final class RolecraftCore extends JavaPlugin {
	private DataStore dataStore;
	private DataManager dataManager;

	@Override
	public void onEnable() {
		final FileConfiguration config = getConfig();
		final String dbType = config.getString("data-storage-solution",
				"sqlite").toLowerCase();

		if (dbType.equals("sqlite")) {
			dataStore = new SQLiteDataStore();
		} else if (dbType.equals("mysql")) {
			dataStore = new MySQLDataStore();
		}
	}

	@Override
	public void onDisable() {
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public DataManager getDataManager() {
		return dataManager;
	}
}
