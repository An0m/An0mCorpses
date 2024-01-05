package dev.an0m.an0mcorpse.corpse;

import dev.an0m.an0mcorpse.An0mCorpse;
import dev.an0m.an0mcorpse.Utils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Corpse extends Npc {

    private final Player sourcePlayer;
    private final String ownerName;
    protected final Set<Player> shownToPlayers = new HashSet<>();
    private final Inventory inventory;
    private final LivingEntity hitbox;

    private int holdenExp;

    /** Use CorpseManger.create(). Thanks! */
    protected Corpse(Player sourcePlayer) {
        super((CraftPlayer) sourcePlayer);
        this.sourcePlayer = sourcePlayer;
        this.ownerName = sourcePlayer.getName();

        // Create copy of player's inventory
        inventory = Bukkit.createInventory(npc.getBukkitEntity(), 9 * 5, sourcePlayer.getDisplayName() + "§r's corpse"); // TODO: Move to config
        inventory.setStorageContents(sourcePlayer.getInventory().getStorageContents());

        // Add armor and offhand
        PlayerInventory sourceInventory = sourcePlayer.getInventory();
        int c = 0;
        for (EquipmentSlot es : new EquipmentSlot[]{EquipmentSlot.OFF_HAND, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD})
            inventory.setItem(40 + c++, sourceInventory.getItem(es));

        // Spawn hitbox entity
        hitbox = (Turtle) sourcePlayer.getWorld().spawnEntity(getLocation(), EntityType.TURTLE);
        hitbox.setAI(false);
        hitbox.setPersistent(true); // Despawned when removing
        Utils.makeInvulnerable(hitbox);
        hitbox.setInvisible(true);
    }

    public Inventory getInventory() {
        return inventory;
    }
    public String getOwnerName() {
        return ownerName;
    }

    public BukkitTask scheduleRemoval(long ticks) {
        return Bukkit.getServer().getScheduler().runTaskLater(An0mCorpse.getInstance(), () -> CorpseManager.remove(getId()), ticks);
    }
    public BukkitTask scheduleRemoval() {
        return scheduleRemoval(3 * 60 * 20); //TODO move to config
    }

    public Player getSourcePlayer() {
        return sourcePlayer;
    }

    public int getId() {
        return npc.getId();
    }

    public void holdExp(int exp) {
        holdenExp = exp;
    }

    public int withdrawExp() {
        int t = holdenExp;
        holdenExp = 0;
        return t;
    }

    public LivingEntity getHitbox() {
        return hitbox;
    }

    /** @return If at least one change from the list of players that could see the corpse has been made */
    public boolean hideFrom(Collection<Player> players) {
        if (!shownToPlayers.removeAll(players)) return false;

        despawn(players);
        return true;
    }
    /** @return If the player can NO LONGER see the corpse */
    public boolean hideFrom(Player player) {
        return hideFrom(Collections.singleton(player));
    }

    /** @return If at least one change from the list of players that could see the corpse has been made */
    public boolean showTo(Collection<Player> players) {
        if (!shownToPlayers.addAll(players)) return false;

        despawn(players);
        return true;
    }
    /** @return If the player can NO LONGER see the corpse */
    public boolean showTo(Player player) {
        return showTo(Collections.singleton(player));
    }

    public Set<Player> getShownTo() {
        return shownToPlayers;
    }

}
