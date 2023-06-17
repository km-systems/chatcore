package us.km127pl.chatcore.commands.chat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Messages;

import java.util.Objects;

@CommandAlias("reply|r")
public class ReplyCommand extends BaseCommand {

    @Dependency
    private ChatCore plugin;

    @Default
    public void onDefault(Player player) {
        player.sendMessage(Messages.deserialize("<text>Usage: <peach>/reply <teal><message>"));
    }

    @Default
    public void onDefault(Player player, String message) {
        // check if message is not ascii
        if (!Messages.isAscii(message) && ChatCore.configuration.getBoolean("chat.settings.only-ascii")) {
            player.sendMessage(Messages.getConfigValue("messages.only-ascii", true));
            return;
        }
        if (!ChatCore.recentMessages.containsKey(player.getUniqueId())) {
            player.sendMessage(Messages.getConfigValue("messages.no-recent-messages", true));
            return;
        }
        Player target = plugin.getServer().getPlayer(ChatCore.recentMessages.get(player.getUniqueId()));
        if (target == null) {
            player.sendMessage(Messages.getConfigValue("messages.player-not-found", true));
            return;
        }
        if (Objects.equals(target.getPlayer(), player)) {
            player.sendMessage(Messages.deserialize("<text>You <peach>» <teal>Yourself<peach>: <text>" + message)); // idk why you would want to do this but ok
            return;
        }
        player.sendMessage(Messages.deserialize("<text>You <peach>» <teal>" + target.getName() + "<peach>: <text>" + message));
        target.sendMessage(Messages.deserialize("<text>" + player.getName() + " <peach>» <teal>You<peach>: <text>" + message));
    }
}
