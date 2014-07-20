package com.github.rolecraftdev.data.storage;

import com.github.rolecraftdev.data.PlayerData;

public abstract class DataStore {
	
	public static final String pt = "playertable";
	public static final String mdt = "metadatatable";
	
	public abstract void intialise();
	
	public abstract void requestPlayerData(final PlayerData callback);
	
	public abstract void commitPlayerData(final PlayerData commit);

}
