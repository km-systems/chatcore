package us.km127pl.chatcore.commands.chat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Messages;

@CommandAlias("shout")
@CommandPermission("chatcore.shout|chatcore.*")
public class ShoutCommand extends BaseCommand {

    @Dependency
    private ChatCore plugin;

    @Default
    public void onDefault(Player player) {
        player.sendMessage(Messages.deserialize("<text>Usage: <peach>/shout <teal><message>"));
    }

    @Default
    public void onDefault(Player player, String message) {
        // check if player is in a ranged channel
        if (plugin.chatChannelManager.chatChannels.get(plugin.chatChannelManager.playerChannels.get(player.getUniqueId())).range == -1) {
            player.sendMessage(Messages.deserialize("<text>You are not in a ranged channel"));
            return;
        }
        String format = Messages.getConfigValue("chat.settings.shout.format");
        int range = plugin.getConfig().getInt("chat.settings.shout.range", 100);
        format = PlaceholderAPI.setPlaceholders(player, format);
        format = format.replace("{message}", message);

        Player[] players = player.getNearbyEntities(range, range, range).stream().filter(entity -> entity instanceof Player).map(entity -> (Player) entity).toArray(Player[]::new);

        for (Player p : players) {
            p.sendMessage(Messages.deserialize(format));
        }

        // send to self
        player.sendMessage(Messages.deserialize(format));

    }
}
