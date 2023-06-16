package us.km127pl.chatcore.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Colors;

public class ChatListener implements Listener {

    private final ChatCore plugin;

    public ChatListener(ChatCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        FileConfiguration config = this.plugin.configuration;

        String format = config.getString("chat.format", "<red>*<reset><white>[<red>%Player_name%<white>] <reset>{message}");
        String message = ChatCore.getMM().serialize(event.message());

        format = PlaceholderAPI.setPlaceholders(event.getPlayer(), format);
        format = format.replace("{message}", message);

        // disables 1.19+ chat reports
        event.setCancelled(true);

        // sends the message to all players
        Bukkit.broadcast(Colors.parseColors(format));
    }
}
