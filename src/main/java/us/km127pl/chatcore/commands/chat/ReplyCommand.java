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

    static boolean checkIfIgnored(Player player, String message, ChatCore plugin, Player target) {
        if (plugin.ignoreListManager.isIgnored(target.getUniqueId(), player.getUniqueId())) {
            // one sided ignore
            player.sendMessage(Messages.deserialize("<text>You <peach>» <teal>" + target.getName() + "<peach>: <text>" + message));
            // we don't want to send the message to the target, as they have the sender ignored lol
            return true;
        }

        if (plugin.ignoreListManager.isIgnored(player.getUniqueId(), target.getUniqueId())) {
            // one sided ignore
            player.sendMessage(Messages.getConfigValue("messages.you-have-ignored", true));
            return true;
        }
        return false;
    }

    @Default
    public void onDefault(Player player) {
        player.sendMessage(Messages.deserialize("<text>Usage: <peach>/reply <teal><message>"));
    }

    @Default
    public void onDefault(Player player, String message) {
        // check if message is not ascii
        if (Messages.isNonASCII(message) && ChatCore.configuration.getBoolean("chat.settings.only-ascii")) {
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
        if (checkIfIgnored(player, message, plugin, Objects.requireNonNull(target.getPlayer()))) return;
        if (Objects.equals(target.getPlayer(), player)) {
            player.sendMessage(Messages.deserialize("<text>You <peach>» <teal>Yourself<peach>: <text>" + message)); // idk why you would want to do this but ok
            return;
        }
        player.sendMessage(Messages.deserialize("<text>You <peach>» <teal>" + target.getName() + "<peach>: <text>" + message));
        target.sendMessage(Messages.deserialize("<text>" + player.getName() + " <peach>» <teal>You<peach>: <text>" + message));
    }
}
