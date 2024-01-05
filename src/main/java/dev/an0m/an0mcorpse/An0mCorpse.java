package dev.an0m.an0mcorpse;

import dev.an0m.an0mcorpse.commands.SpawnCmd;
import dev.an0m.an0mcorpse.corpse.CorpseManager;
import dev.an0m.an0mcorpse.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class An0mCorpse extends JavaPlugin {

    private static An0mCorpse instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginCommand("corpse").setExecutor(new SpawnCmd());
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Add and remove the corpses for the nearby players (run every tick)
        getServer().getScheduler().scheduleSyncRepeatingTask(this, CorpseManager::updateCorpseShowPlayers, 0, 1L);
    }

    @Override
    public void onDisable() {
    }

    public static An0mCorpse getInstance() {
        return instance;
    }
}
