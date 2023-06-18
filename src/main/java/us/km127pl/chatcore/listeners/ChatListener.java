package us.km127pl.chatcore.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.ChatChannelManager;
import us.km127pl.chatcore.utility.Messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private final ChatCore plugin;

    public ChatListener(ChatCore chatCore) {
        this.plugin = chatCore;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        FileConfiguration config = ChatCore.configuration;
        String channelName = plugin.chatChannelManager.playerChannels.get(event.getPlayer().getUniqueId());
        ChatChannelManager.ChannelInfo channel = plugin.chatChannelManager.chatChannels.get(channelName);

        // if no channel, set to default
        if (channelName == null) {
            channelName = plugin.chatChannelManager.defaultChannel;
            channel = plugin.chatChannelManager.chatChannels.get(channelName);
        }

        String format = channel.format;
        String message = ChatCore.getMM().serialize(event.message()); // gets the message from the event


        format = PlaceholderAPI.setPlaceholders(event.getPlayer(), format); // sets placeholders in the format
        format = format.replace("{message}", message); // replaces {message} with the message

        if (config.getBoolean("chat.settings.pings.enabled")) {
            // replace all "@..." in the format with the lowercase
            String regex = "\\b@\\w+\\b"; // Regex pattern to match words starting with "@"

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(format);

            while (matcher.find()) {
                String match = matcher.group();
                format = format.replace(match, match.toLowerCase());
            }

            // get all players and replace `@player` with their name
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (format.contains("@" + player.getName().toLowerCase()) && config.getBoolean("chat.settings.pings.ding")) {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 3, 3);
                }
                format = format.replace("@" + player.getName().toLowerCase(), Messages.getConfigValue("chat.settings.pings.format")
                        .replace("<player>", player.getName()
                                .replace("<sender>", event.getPlayer().getName())
                        ));
            }

        }

        // disables 1.19+ chat reports
        event.setCancelled(true);

        // sends the message to all players in the channel
        for (Player player : Bukkit.getOnlinePlayers()) {
            // check if player even has a channel set, if no, set to default
            plugin.chatChannelManager.playerChannels.computeIfAbsent(player.getUniqueId(), k -> plugin.chatChannelManager.defaultChannel);
            // check if player is in the channel
            if (plugin.chatChannelManager.playerChannels.get(player.getUniqueId()).equals(channelName)) {
                player.sendMessage(Messages.deserialize(format));
            }

            // also check for the "receives" part
            if (channel.receives != null) {
                for (String receive : channel.receives) {
                    if (plugin.chatChannelManager.playerChannels.get(player.getUniqueId()).equals(receive)) {
                        player.sendMessage(Messages.deserialize(format));
                    }
                }
            }
        }

        // log the message without colors
        Bukkit.getLogger().info(Messages.stripColours(format));
    }
}
