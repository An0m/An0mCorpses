package dev.an0m.an0mcorpse.listeners;

import dev.an0m.an0mcorpse.corpse.Corpse;
import dev.an0m.an0mcorpse.corpse.CorpseManager;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

public class PlayerListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEntityEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        // Check if the entity is a corpse
        Corpse corpse = CorpseManager.getCorpse(e.getRightClicked());
        if (corpse == null) return;

        Player player = e.getPlayer();
        player.openInventory(corpse.getInventory());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        Corpse corpse = CorpseManager.create(e.getEntity());
        if (corpse == null) return;

        e.getDrops().clear();
        corpse.holdExp(e.getDroppedExp());
        corpse.scheduleRemoval();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent e) {
        Optional<Corpse> corpse = CorpseManager.getCorpses().stream().filter(c -> c.npc.getBukkitEntity() == e.getInventory().getHolder()).findFirst();
        if (corpse.isEmpty()) return;

        ((CraftPlayer) e.getPlayer()).giveExp(corpse.get().withdrawExp());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!e.getInventory().isEmpty()) return;

        // Check if the inventory holder is a corpse
        Optional<Corpse> corpse = CorpseManager.getCorpses().stream().filter(c -> c.npc.getBukkitEntity() == e.getInventory().getHolder()).findFirst();
        if (corpse.isEmpty()) return;

        CorpseManager.remove(corpse.get().getId());
    }

}
