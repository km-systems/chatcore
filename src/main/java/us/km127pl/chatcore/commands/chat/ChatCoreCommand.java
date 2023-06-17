package us.km127pl.chatcore.commands.chat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Messages;

@CommandAlias("chatcore")
@CommandPermission("chatcore.manage|chatcore.*")
public class ChatCoreCommand extends BaseCommand {

    @Dependency
    private ChatCore plugin;

    @Default
    @HelpCommand
    public void onDefault(Player player) {
        // help message
        player.sendMessage(Messages.deserialize("Help", true));

        // commands
        player.sendMessage(Messages.deserialize("<text>- Commands:"));
        player.sendMessage(Messages.deserialize("<text>/chatcore reload <peach>- <text>Reloads the plugin."));
        player.sendMessage(Messages.deserialize("<text>/chatcore help <peach>- <text>Shows this help message."));
    }

    @Subcommand("reload")
    @CommandPermission("chatcore.manage|chatcore.reload")
    public void onReload(Player player) {
        // reload the plugin
        player.sendMessage(Messages.deserialize("reloaded!", true));
        this.plugin.reload();
    }
}