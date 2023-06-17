package us.km127pl.chatcore.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.utility.Messages;

@CommandAlias("broadcast|bc")
@CommandPermission("chatcore.broadcast|chatcore.*")
public class BroadcastCommand extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        player.sendMessage(Messages.deserialize("<text>Usage: <peach>/broadcast <teal><message>"));
    }

    @Default
    public void onDefault(Player player, String message) {
        String format = Messages.getConfigValue("chat.settings.broadcast.format");
        String formattedMessage = format.replace("{message}", message);
        Bukkit.broadcast(Messages.deserialize(formattedMessage));
    }
}
