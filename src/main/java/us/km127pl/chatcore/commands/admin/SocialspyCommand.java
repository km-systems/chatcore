package us.km127pl.chatcore.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Messages;

@CommandAlias("socialspy|ss")
@CommandPermission("chatcore.socialspy|chatcore.*")
public class SocialspyCommand extends BaseCommand {
    @Dependency
    private ChatCore plugin;

    @Default
    public void onDefault(Player player) {
        // toggle socialspy
        if (!plugin.chatChannelManager.socialSpy.containsKey(player) || plugin.chatChannelManager.socialSpy.get(player)) {
            plugin.chatChannelManager.socialSpy.put(player, true);
            player.sendMessage(Messages.deserialize("<teal>SocialSpy <peach>enabled.", true));
            return;
        } else {
            plugin.chatChannelManager.socialSpy.put(player, true);
            player.sendMessage(Messages.deserialize("<teal>SocialSpy <peach>enabled.", true));
        }
    }
}
