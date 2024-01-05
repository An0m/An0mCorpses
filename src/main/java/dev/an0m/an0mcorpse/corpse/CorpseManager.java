package dev.an0m.an0mcorpse.corpse;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class CorpseManager {
    private final static HashMap<Integer, Corpse> corpses = new HashMap<>();
    private final static HashMap<Entity, Corpse> hitboxes = new HashMap<>();

    public static Collection<Corpse> getCorpses() {
        return corpses.values();
    }

    /** Get spawned corpse from corpse's entityId */
    public static Corpse getCorpse(int id) {
        return corpses.get(id);
    }
    /** Get spawned corpse from corpse or hitbox entity */
    public static Corpse getCorpse(Entity entity) {
        Corpse corpse = corpses.get(entity.getEntityId());
        if (corpse != null) return corpse;

        return hitboxes.get(entity);
    }

    /** Removes a corpse */
    public static boolean remove(int id) {
        Corpse corpse = corpses.get(id);
        if (corpse == null) return false;

        hitboxes.remove(corpse.getHitbox());
        corpse.getHitbox().remove(); // The npc keeps the chunk loaded
        corpse.despawn();
        corpses.remove(id);
        new ArrayList<>(corpse.getInventory().getViewers()).forEach(HumanEntity::closeInventory); // This MUST be after corpses.remove, or else it will crash the server
        return true;
    }

    /**
     * Spawn the corpse of a player
     * @return The corpse or null if unable to spawn (void)
     */
    public static Corpse create(Player sourcePlayer) {
        Corpse corpse = new Corpse(sourcePlayer);
        if (corpse.npc == null) return null;

        corpse.spawn();
        corpses.put(corpse.getId(), corpse);
        hitboxes.put(corpse.getHitbox(), corpse);
        return corpse;
    }

    /**
     * Updates which player can see which corpse
     * Called every tick
     * */
    public static void updateCorpseShowPlayers() {
        for (Corpse corpse : getCorpses()) {
            Set<Player> players = corpse.getNearbyPlayers();

            // Find new players
            Set<Player> newPlayers = new HashSet<>(players);
            newPlayers.removeAll(corpse.shownToPlayers);
            corpse.spawn(newPlayers);

            // Players gone away
            Set<Player> gonePlayers = new HashSet<>(corpse.shownToPlayers);
            gonePlayers.removeAll(players);
            corpse.despawn(gonePlayers);

            corpse.shownToPlayers.addAll(newPlayers);
            corpse.shownToPlayers.removeAll(gonePlayers);
        }
    }
}
