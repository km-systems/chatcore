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
import us.km127pl.chatcore.utility.Messages;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private final ChatCore plugin;

    public ChatListener(ChatCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        FileConfiguration config = ChatCore.configuration;

        String format = config.getString("chat.format", "<red>*<reset><white>[<red>%Player_name%<white>] <reset>{message}"); // gets the format from the config
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

        // sends the message to all players
        Bukkit.broadcast(Messages.deserialize(format));
    }
}
