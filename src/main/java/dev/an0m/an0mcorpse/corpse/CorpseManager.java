package dev.an0m.an0mcorpse.corpse;

import org.bukkit.entity.Player;

import java.util.*;

public class CorpseManager {
    private final static Map<Integer, Corpse> corpses = new HashMap<>();

    public static Collection<Corpse> getCorpses() {
        return corpses.values();
    }

    public static Corpse getCorpse(int id) {
        return corpses.get(id);
    }

    public static Corpse create(Player sourcePlayer) {
        Corpse corpse = new Corpse(sourcePlayer);
        corpse.spawn();
        corpses.put(corpse.getId(), corpse);
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
            corpse.remove(gonePlayers);

            corpse.shownToPlayers.addAll(newPlayers);
            corpse.shownToPlayers.removeAll(gonePlayers);
        }
    }
}
