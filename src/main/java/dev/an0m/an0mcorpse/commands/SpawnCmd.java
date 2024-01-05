package dev.an0m.an0mcorpse.commands;

import dev.an0m.an0mcorpse.corpse.CorpseManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static dev.an0m.an0mcorpse.Utils.sendMessage;

public class SpawnCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length == 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sendMessage(sender, "&cPlayer not found");
                return true;
            }
        }
        else if (args.length == 0 && sender instanceof Player)
            target = (Player) sender;
        else return false;

        CorpseManager.create(target).scheduleRemoval();
        sendMessage(sender, "&aDone!");
        return false;
    }
}
