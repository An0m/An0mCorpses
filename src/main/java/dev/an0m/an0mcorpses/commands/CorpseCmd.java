package dev.an0m.an0mcorpses.commands;

import dev.an0m.an0mcorpses.An0mCorpses;
import dev.an0m.an0mcorpses.Utils;
import dev.an0m.an0mcorpses.corpse.Corpse;
import dev.an0m.an0mcorpses.corpse.CorpseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static dev.an0m.an0mcorpses.Utils.sendMessage;

public class CorpseCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return false;

        Player target;
        switch (args[0]) {
            case "spawn":
                target = runSimpleTargetCmd(args, "corpse spawn", sender);
                if (target != null) {
                    CorpseManager.create(target, sender instanceof Player
                            ? ((Player) sender).getLocation()
                            : target.getLocation()
                    ).scheduleRemoval();
                    sendMessage(sender, "&aSpawned " + target.getDisplayName() + "&a's corpse");
                }
                return true;
            case "remove":
                target = runSimpleTargetCmd(args, "corpse remove", sender);
                if (target != null) {
                    int count = 0;
                    for (Corpse corpse : CorpseManager.getCorpses()) {
                        if (corpse.getSourcePlayer() != target) continue;
                        count++;
                        CorpseManager.remove(corpse);
                    }
                    sendMessage(sender, count == 0
                            ? "&cNo " + target.getDisplayName() + "'s corpses has been found"
                            : "&aRemoved all " + target.getDisplayName() + "&a's corpses &7(" + count + ")"
                    );
                }
                return true;
            case "clear":
                new ArrayList<>(CorpseManager.getCorpses()).forEach(CorpseManager::remove);
                sendMessage(sender, "&aAll spawned corpses have been removed");
                return true;
            case "reload":
            case "rl":
                An0mCorpses.getInstance().reloadConfig();
                sendMessage(sender, "&aThe plugin config has been reloaded!");
                return true;
            default:
                return false;
        }
    }

    private Player runSimpleTargetCmd(String[] args, String command, CommandSender sender) {
        if (args.length != 2) {
            sendMessage(sender, "&cUsage: /" + command + " <player>");
            return null;
        }

        Player target = Utils.getPlayer(args[1]);
        if (target == null)
            sendMessage(sender, "&cPlayer not found");
        return target;
    }
}
