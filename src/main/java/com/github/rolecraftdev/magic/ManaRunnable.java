package com.github.rolecraftdev.magic;


import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import org.bukkit.scheduler.BukkitRunnable;

public class ManaRunnable extends BukkitRunnable {

    private final RolecraftCore parent;

    public ManaRunnable (RolecraftCore parent) {
        this.parent = parent;
    }
    
    @Override
    public void run() {
        for(PlayerData pd : parent.getDataManager().getPlayerDatum()) {
            pd.addMana(pd.getManaRegenRate());
        }
    }
}
