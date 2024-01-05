package dev.an0m.an0mcorpses.events;

import dev.an0m.an0mcorpses.corpse.Corpse;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/** Called when a player death causes a corpse to be created */
public class CorpseGenerateEvent extends CorpseCreateEvent {
    private static final HandlerList handlers = new HandlerList();
    private final List<ItemStack> droppedItems;
    private int droppedExp;
    private boolean expSupport;
    private String deathMessage;

    public CorpseGenerateEvent(Corpse corpse, List<ItemStack> droppedItems, int droppedExp, boolean expSupport, String deathMessage) {
        super(corpse);
        this.droppedItems = droppedItems;
        this.droppedExp = droppedExp;
        this.expSupport = expSupport;
        this.deathMessage = deathMessage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public final List<ItemStack> getDrops() {
        return droppedItems;
    }
    public final int getDroppedExp() {
        return droppedExp;
    }
    public final int setDroppedExp(int v) {
        return droppedExp = v;
    }

    public final String getDeathMessage() {
        return deathMessage;
    }
    public final void setDeathMessage(String s) {
        deathMessage = s;
    }

    public final boolean isExpSupported() {
        return this.expSupport;
    }
    public final void setExpSupport(boolean b) {
        expSupport = b;
    }

}
