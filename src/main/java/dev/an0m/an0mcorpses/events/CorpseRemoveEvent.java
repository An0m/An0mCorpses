package dev.an0m.an0mcorpses.events;


import dev.an0m.an0mcorpses.corpse.Corpse;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** Called when a corpse is created */
public class CorpseRemoveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Corpse corpse;
    private boolean cancelled = false;

    public CorpseRemoveEvent(Corpse corpse) {
        this.corpse = corpse;
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
    public final Player getSourcePlayer() {
        return corpse.getSourcePlayer();
    }
    public final Location getLocation() {
        return corpse.getLocation();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
