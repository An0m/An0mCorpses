package dev.an0m.an0mcorpse.corpse;

import dev.an0m.an0mcorpse.An0mCorpse;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;

public class Corpse extends Npc {

    private final Player sourcePlayer;
    public final HashSet<Player> shownToPlayers = new HashSet<>();

    /** Use CorpseManger.create(). Thanks! */
    protected Corpse(Player sourcePlayer) {
        super((CraftPlayer) sourcePlayer);
        this.sourcePlayer = sourcePlayer;
    }

    public BukkitTask scheduleRemoval(long ticks) {
        return Bukkit.getServer().getScheduler().runTaskLater(An0mCorpse.getInstance(), () -> remove(), ticks);
    }

    public Player getSourcePlayer() {
        return sourcePlayer;
    }

    public int getId() {
        return npc.getId();
    }

}
