package us.km127pl.chatcore.listeners;

import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Messages;

public class CommandPreprocessListener implements Listener {

    private final ChatCore plugin;

    public CommandPreprocessListener(ChatCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        String command = args[0].replace("/", "");

        switch (command) {
            case "msg":
            case "tell":
            case "whisper":
            case "w":
                event.setCancelled(true);
                String targetName = args[1];
                Player target = plugin.getServer().getPlayer(args[1]);
                Player player = event.getPlayer();
                if (target == null) {
                    player.sendMessage(Messages.replaceTemplate(
                            Messages.getConfigValue("messages.player-not-found", true),
                            "<player>",
                            Component.text(targetName)
                    ));
                    return;
                }
                if (!target.isOnline()) {
                    player.sendMessage(Messages.replaceTemplate(
                            Messages.getConfigValue("messages.player-not-online", true),
                            "<player>",
                            Component.text(targetName)
                    ));
                    return;
                }
                if (args.length == 2) {
                    ChatCore.messageCommand.onDefault(event.getPlayer());
                    return;
                }
                String message = event.getMessage().replace(args[0] + " " + args[1] + " ", "");

                ChatCore.messageCommand.onDefault(player, new OnlinePlayer(target), message);
                break;
        }
    }
}
