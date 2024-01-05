package dev.an0m.an0mcorpse.corpse;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import dev.an0m.an0mcorpse.An0mCorpse;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.BoundingBox;

import java.util.*;
import java.util.stream.Collectors;


public class Npc {
    public final EntityPlayer npc;

    protected Npc(CraftPlayer sourcePlayer) {
        EntityPlayer playerHandle = sourcePlayer.getHandle();

        // Get ground position
        Location location = sourcePlayer.getLocation().clone();
        BoundingBox bb; Block block;
        while (location.getY() > -64 && ((block = location.getBlock()).isPassable() || (bb = block.getBoundingBox()).getWidthX() < .5 || bb.getWidthZ() < .5))
            location.subtract(0, 1, 0);
        location.setY(location.getBlock().getBoundingBox().getMaxY() - 0.15); // Position the body ON the ground. Not partially underground

        // Create the npc profile
        UUID uuid = UUID.randomUUID();
        GameProfile npcProfile = new GameProfile(uuid, uuid.toString().replace("-", "").substring(0, 16)); // Random name (16 chars is the max name length)
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        npc = new EntityPlayer(minecraftServer, worldServer, npcProfile, new PlayerInteractManager(worldServer));

        npc.setPose(EntityPose.SWIMMING);
        npc.setCustomNameVisible(false);
        npc.setPosition(location.getX(), location.getY(), location.getZ());
        npc.setArrowCount(sourcePlayer.getArrowsInBody());

        // Make invulnerable (somehow EntityPlayer#setInvulnerable doesn't work?)
        npc.maxNoDamageTicks = Integer.MAX_VALUE; // Almost 7 years
        npc.noDamageTicks = Integer.MAX_VALUE;

        // Inventory
        for (int i = 0; i < playerHandle.inventory.items.size(); i++)
            npc.inventory.items.set(i, playerHandle.inventory.items.get(i).cloneItemStack());
        for (int i = 0; i < playerHandle.inventory.armor.size(); i++)
            npc.inventory.armor.set(i, playerHandle.inventory.armor.get(i).cloneItemStack());
        for (int i = 0; i < playerHandle.inventory.extraSlots.size(); i++)
            npc.inventory.extraSlots.set(i, playerHandle.inventory.extraSlots.get(i).cloneItemStack());
        npc.inventory.itemInHandIndex = sourcePlayer.getInventory().getHeldItemSlot();

        // Skin and main hand
        DataWatcher dataWatcher = npc.getDataWatcher();
        dataWatcher.set(DataWatcherRegistry.a.a(16), (byte)127); // Enable all skin layers
        dataWatcher.set(DataWatcherRegistry.a.a(17), (byte) (npc.getMainHand() == EnumMainHand.LEFT ? 0 : 1)); // Change main hand (https://wiki.vg/Protocol#:~:text=the%20Statistics%20menu.-,Client%20Information)

        // Skin
        try {
            Property textures = sourcePlayer.getProfile().getProperties().get("textures").iterator().next();
            npcProfile.getProperties().put("textures", new Property("textures", textures.getValue(), textures.getSignature()));
        } catch (NoSuchElementException ignored) {} // No skin

        // Add to the world (no clue if it's needed or not)
        new PlayerConnection(minecraftServer, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), npc);
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        world.addEntity(npc, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public void spawn(Collection<Player> targets) {
        for (Player target : targets) {
            CraftPlayer craftPlayer = (CraftPlayer) target;
            PlayerConnection connection = craftPlayer.getHandle().playerConnection;

            // Add entity
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));

            // Inventory
            connection.sendPacket(new PacketPlayOutEntityEquipment(npc.getId(), Arrays.asList(
                    new Pair<>(EnumItemSlot.MAINHAND, npc.getItemInMainHand()),
                    new Pair<>(EnumItemSlot.OFFHAND, npc.getItemInOffHand()),
                    new Pair<>(EnumItemSlot.HEAD, npc.getEquipment(EnumItemSlot.HEAD)),
                    new Pair<>(EnumItemSlot.CHEST, npc.getEquipment(EnumItemSlot.CHEST)),
                    new Pair<>(EnumItemSlot.LEGS, npc.getEquipment(EnumItemSlot.LEGS)),
                    new Pair<>(EnumItemSlot.FEET, npc.getEquipment(EnumItemSlot.FEET))
            )));

            // Send info like the skin layers, the pose (not sure?) and the main hand
            connection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true));
            hideNpcNametag(craftPlayer);

            // Remove from tablist
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(An0mCorpse.getInstance(), () ->
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc)),
            10L);
        }
    }
    public void spawn() {
        spawn(getNearbyPlayers());
    }

    private void hideNpcNametag(CraftPlayer player) {
        CraftScoreboardManager scoreboardManager = ((CraftServer) Bukkit.getServer()).getScoreboardManager();
        assert scoreboardManager != null;
        CraftScoreboard mainScoreboard = scoreboardManager.getMainScoreboard();
        Scoreboard scoreboard = mainScoreboard.getHandle();

        String name = npc.getName();
        ScoreboardTeam team = scoreboard.getTeam(name);
        if (team == null) team = new ScoreboardTeam(scoreboard, name);

        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        team.setCollisionRule(ScoreboardTeamBase.EnumTeamPush.NEVER);

        PlayerConnection connection = player.getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
        connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
        connection.sendPacket(new PacketPlayOutScoreboardTeam(team, Collections.singletonList(name), 3));
    }

    public void remove(Collection<Player> targets) {
        for (Player target : targets) {
            try {
                PlayerConnection connection = ((CraftPlayer) target).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
            } catch (Exception ignored) {}
        }
    }
    public void remove() {
        remove(getNearbyPlayers());
    }

    public Set<Player> getNearbyPlayers() {
        return npc.getBukkitEntity().getNearbyEntities(45, 45, 45).stream().filter(e -> e instanceof Player).map(p -> (Player) p).collect(Collectors.toSet());
    }

}
