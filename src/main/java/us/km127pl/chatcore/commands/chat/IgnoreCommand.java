package us.km127pl.chatcore.commands.chat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Messages;

import java.util.UUID;

@CommandAlias("ignore|unignore")
public class IgnoreCommand extends BaseCommand {

    @Dependency
    private ChatCore plugin;

    @Default
    @CommandCompletion("@players|list")
    public void onDefault(Player player) {
        player.sendMessage(Messages.deserialize("<text>Usage: <peach>/ignore <teal><player>"));
    }

    @Default
    public void onDefault(Player player, OnlinePlayer toIgnore) {
        // check if the player can ignore
        if (toIgnore.getPlayer().hasPermission("chatcore.ignore.bypass")) {
            player.sendMessage(Messages.deserialize(Messages.getConfigValue("messages.may-not-ignore").replace("<player>", toIgnore.getPlayer().getName())));
            return;
        }
        if (plugin.ignoreListManager.isIgnored(player.getUniqueId(), toIgnore.getPlayer().getUniqueId())) {
            // unignore
            plugin.ignoreListManager.remove(player.getUniqueId(), toIgnore.getPlayer().getUniqueId());
            player.sendMessage(Messages.deserialize("<text>You are no longer ignoring <peach>" + toIgnore.getPlayer().getName()));
            return;
        }
        // ignore
        plugin.ignoreListManager.add(player.getUniqueId(), toIgnore.getPlayer().getUniqueId());
        player.sendMessage(Messages.deserialize("<text>You are now ignoring <peach>" + toIgnore.getPlayer().getName()));
    }

    @Subcommand("list")
    public void onList(Player player) {
        if (!plugin.ignoreListManager.ignoreList.containsKey(player.getUniqueId())) {
            player.sendMessage(Messages.deserialize("<text>You are not ignoring anyone.", true));
            return;
        }
        player.sendMessage(Messages.deserialize("<text>Ignored players:", true));
        for (UUID ignored : plugin.ignoreListManager.ignoreList.get(player.getUniqueId())) {
            player.sendMessage(Messages.deserialize("<text> - <peach>" + Bukkit.getOfflinePlayer(ignored).getName()));
        }
    }

}
