package com.github.rolecraftdev.data;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.storage.DataStore;

public final class DataManager {
	private final RolecraftCore plugin;
	private final DataStore store;

	public DataManager(final RolecraftCore plugin, final DataStore store) {
		this.plugin = plugin;
		this.store = store;
	}

	public RolecraftCore getPlugin() {
		return plugin;
	}

	public DataStore getStore() {
		return store;
	}
}
