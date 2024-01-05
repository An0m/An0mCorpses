package dev.an0m.an0mcorpse;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {
    public static String cc(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(CommandSender target, String message) {
        if (message != null && !message.isEmpty())
            target.sendMessage(cc(message));
    }
}
