package dev.an0m.an0mcorpses;

import dev.an0m.an0mcorpses.commands.SpawnCmd;
import dev.an0m.an0mcorpses.corpse.CorpseManager;
import dev.an0m.an0mcorpses.listeners.PlayerListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class An0mCorpses extends JavaPlugin {

    private static An0mCorpses instance;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();

        getServer().getPluginCommand("corpse").setExecutor(new SpawnCmd());
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Add and remove the corpses for the nearby players (run every tick)
        getServer().getScheduler().scheduleSyncRepeatingTask(this, CorpseManager::updateCorpseShowPlayers, 0, 1L);
    }

    public static An0mCorpses getInstance() {
        return instance;
    }
}
