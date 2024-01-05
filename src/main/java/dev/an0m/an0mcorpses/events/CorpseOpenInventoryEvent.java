package dev.an0m.an0mcorpses.events;

import dev.an0m.an0mcorpses.corpse.Corpse;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;

public class CorpseOpenInventoryEvent extends InventoryOpenEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Corpse corpse;
    private boolean expSupport;

    public CorpseOpenInventoryEvent(Corpse corpse, InventoryView transaction, boolean expSupport) {
        super(transaction);
        this.corpse = corpse;
        this.expSupport = expSupport;
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

    public final boolean isExpSupported() {
        return expSupport;
    }
    public final void setExpSupport(boolean b) {
        expSupport = b;
    }
}
