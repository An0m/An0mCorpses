package dev.an0m.an0mcorpses.events;

import dev.an0m.an0mcorpses.corpse.Corpse;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

public class CorpseCloseInventoryEvent extends InventoryCloseEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Corpse corpse;
    private boolean removeOnEmpty;

    public CorpseCloseInventoryEvent(Corpse corpse, InventoryView transaction, boolean removeOnEmpty) {
        super(transaction);
        this.corpse = corpse;
        this.removeOnEmpty = removeOnEmpty;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public final Corpse getCorpse() {
        return corpse;
    }

    public final boolean shouldRemoveOnEmpty() {
        return removeOnEmpty;
    }
    public final void setRemoveOnEmpty(boolean b) {
        removeOnEmpty = b;
    }
}
