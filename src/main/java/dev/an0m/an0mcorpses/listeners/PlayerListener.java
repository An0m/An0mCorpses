package dev.an0m.an0mcorpses.listeners;

import dev.an0m.an0mcorpses.An0mCorpses;
import dev.an0m.an0mcorpses.corpse.Corpse;
import dev.an0m.an0mcorpses.corpse.CorpseManager;
import dev.an0m.an0mcorpses.events.CorpseCloseInventoryEvent;
import dev.an0m.an0mcorpses.events.CorpseGenerateEvent;
import dev.an0m.an0mcorpses.events.CorpseOpenInventoryEvent;
import org.bukkit.Bukkit;
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

        // Run event
        CorpseGenerateEvent event = new CorpseGenerateEvent(corpse,
                e.getDrops(), e.getDroppedExp(), An0mCorpses.config.getBoolean("expSupport"), e.getDeathMessage());
        if (corpse == null) event.setCancelled(true);
        Bukkit.getPluginManager().callEvent(event);

        // Alter death event
        if (e.getKeepInventory()) event.setCancelled(true);
        e.setDeathMessage(event.getDeathMessage());
        e.setDroppedExp(event.getDroppedExp());

        // Create corpse
        if (event.isCancelled()) return;
        e.getDrops().clear();
        if (corpse == null) throw new RuntimeException("Unable to modify a null corpse");
        if (event.isExpSupported()) corpse.holdExp(e.getDroppedExp());
        if (event.isRemovalScheduled()) corpse.scheduleRemoval();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent e) {
        Optional<Corpse> corpse = CorpseManager.getCorpses().stream().filter(c -> c.npc.getBukkitEntity() == e.getInventory().getHolder()).findFirst();
        if (corpse.isEmpty()) return;

        // Run event
        CorpseOpenInventoryEvent event = new CorpseOpenInventoryEvent(corpse.get(), e.getView(), An0mCorpses.config.getBoolean("expSupport"));
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        if (event.isExpSupported())
            ((CraftPlayer) event.getPlayer()).giveExp(event.getCorpse().withdrawExp());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!e.getInventory().isEmpty()) return;

        // Check if the inventory holder is a corpse
        Optional<Corpse> corpse = CorpseManager.getCorpses().stream().filter(c -> c.npc.getBukkitEntity() == e.getInventory().getHolder()).findFirst();
        if (corpse.isEmpty()) return;

        // Run event
        CorpseCloseInventoryEvent event = new CorpseCloseInventoryEvent(corpse.get(), e.getView(), An0mCorpses.config.getBoolean("expSupport"));
        Bukkit.getPluginManager().callEvent(event);

        if (event.shouldRemoveOnEmpty())
            CorpseManager.remove(corpse.get().getId());
    }
}
