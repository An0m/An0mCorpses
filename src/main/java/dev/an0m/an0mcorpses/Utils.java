package dev.an0m.an0mcorpses;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Utils {
    public static String cc(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(CommandSender target, String message) {
        if (message != null && !message.isEmpty())
            target.sendMessage(cc(message));
    }

    public static void makeInvulnerable(EntityLiving entity) {
        entity.maxNoDamageTicks = entity.noDamageTicks = Integer.MAX_VALUE; // Almost 7 years
    }
    public static void makeInvulnerable(CraftLivingEntity entity) {
        makeInvulnerable(entity.getHandle());
    }
    public static void makeInvulnerable(LivingEntity entity) {
        makeInvulnerable((CraftLivingEntity) entity);
    }

    public static Player getPlayer(String nameOrUUID) {
        Player target = Bukkit.getPlayerExact(nameOrUUID);
        if (target != null) return target;

        return Bukkit.getPlayer(UUID.fromString(nameOrUUID));
    }

}
