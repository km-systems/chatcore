package us.km127pl.chatcore.listeners;


import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Messages;

public class PlayerConnectionListener implements Listener {

    private ChatCore plugin;

    public PlayerConnectionListener(ChatCore chatCore) {
        this.plugin = chatCore;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.chatChannelManager.playerChannels.put(event.getPlayer().getUniqueId(), plugin.chatChannelManager.defaultChannel);

        // set the join message
        String format = ChatCore.configuration.getString("chat.join-message", "<yellow>%Player_name% joined the game");

        format = PlaceholderAPI.setPlaceholders(event.getPlayer(), format);

        event.joinMessage(Messages.deserialize(format));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // set the leave message
        String format = ChatCore.configuration.getString("chat.quit-message", "<yellow>%Player_name% left the game");

        format = PlaceholderAPI.setPlaceholders(event.getPlayer(), format);

        event.quitMessage(Messages.deserialize(format));
    }
}
