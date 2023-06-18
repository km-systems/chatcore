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

        String format;

        // check if the player has joined before
        if (!event.getPlayer().hasPlayedBefore()) {
            // first time player
            format = ChatCore.configuration.getString("chat.welcome-message", "<yellow>%Player_name% joined the game for the first time");
        } else {
            // set the join message
            format = ChatCore.configuration.getString("chat.join-message", "<yellow>%Player_name% joined the game");
        }

        if (format.equals("")) {
            return; // this part was disabled
        }

        format = PlaceholderAPI.setPlaceholders(event.getPlayer(), format);
        event.joinMessage(Messages.deserialize(format));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // set the leave message
        String format = ChatCore.configuration.getString("chat.quit-message", "<yellow>%Player_name% left the game");

        if (format.equals("")) {
            return; // this part was disabled
        }

        format = PlaceholderAPI.setPlaceholders(event.getPlayer(), format);

        event.quitMessage(Messages.deserialize(format));
    }
}
