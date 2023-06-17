package us.km127pl.chatcore.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Messages;

@CommandAlias("msg|message|tell|whisper|w")
public class MessageCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players @nothing")
    public void onDefault(Player player) {
        // send usage
        player.sendMessage(Messages.deserialize("<text>Usage: <peach>/msg <teal><player> <message>"));
    }

    @Default
    public void onDefault(Player player, OnlinePlayer target, String message) {
        // check if message is not ascii
        if (!Messages.isAscii(message) && ChatCore.configuration.getBoolean("chat.settings.only-ascii")) {
            player.sendMessage(Messages.getConfigValue("messages.only-ascii", true));
            return;
        }
        if (target.getPlayer().equals(player)) {
            player.sendMessage(Messages.deserialize("<text>You <peach>» <teal>Yourself<peach>: <text>" + message)); // idk why you would want to do this but ok
            return;
        }
        player.sendMessage(Messages.deserialize("<text>You <peach>» <teal>" + target.getPlayer().getName() + "<peach>: <text>" + message));
        target.getPlayer().sendMessage(Messages.deserialize("<text>" + player.getName() + " <peach>» <teal>You<peach>: <text>" + message));
    }
}
