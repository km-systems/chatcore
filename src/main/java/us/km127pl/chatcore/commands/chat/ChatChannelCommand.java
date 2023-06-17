package us.km127pl.chatcore.commands.chat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.ChatChannelManager;
import us.km127pl.chatcore.utility.Messages;

@CommandAlias("chat|channel")
public class ChatChannelCommand extends BaseCommand {

    @Dependency
    private ChatCore plugin;

    @Default
    public void onDefault(Player player) {
        player.sendMessage(Messages.deserialize("<text>Usage: <peach>/chat <teal><channel>"));
        // list available channels that we have permission to join
        player.sendMessage(Messages.deserialize("<text>Available channels:", true));
        for (ChatChannelManager.ChannelInfo channel : plugin.chatChannelManager.chatChannels.values()) {
            if (player.hasPermission(channel.permission) || channel.permission.equals("none")) {
                player.sendMessage(Messages.deserialize("<text> - <peach>" + channel.name));
            }
        }
    }

    /*
        TODO: the @channel completion should only show channels that the player has permission to join
        we're waiting for https://github.com/aikar/commands/pull/348 to be merged into the upstream ACF
    */
    @Default
    @CommandCompletion("@channels")
    public void onDefault(Player player, String channel) {
        // check if the channel exists
        if (!plugin.chatChannelManager.chatChannels.containsKey(channel)) {
            player.sendMessage(Messages.deserialize(Messages.getConfigValue("messages.channel-does-not-exist").replace("<channel>", channel)));
            return;
        }
        ChatChannelManager.ChannelInfo channelInfo = plugin.chatChannelManager.chatChannels.get(channel);
        // check if the player has permission to join the channel
        if (!player.hasPermission(channelInfo.permission) && !channelInfo.permission.equals("none")) {
            player.sendMessage(Messages.deserialize(Messages.getConfigValue("messages.no-permission-to-join-channel").replace("<channel>", channel)));
            return;
        }
        // check if the player is already in the channel
        if (plugin.chatChannelManager.playerChannels.containsKey(player.getUniqueId()) && plugin.chatChannelManager.playerChannels.get(player.getUniqueId()).equals(channel)) {
            player.sendMessage(Messages.deserialize(Messages.getConfigValue("messages.already-in-channel").replace("<channel>", channel)));
            return;
        }
        // join the channel
        plugin.chatChannelManager.playerChannels.put(player.getUniqueId(), channel);
        player.sendMessage(Messages.deserialize(Messages.getConfigValue("messages.joined-channel").replace("<channel>", channel)));
    }
}
