package us.km127pl.chatcore.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;
import us.km127pl.chatcore.utility.Messages;

@CommandAlias("alert")
@CommandPermission("chatcore.alert|chatcore.*")
public class AlertCommand extends BaseCommand {

    @Dependency
    private ChatCore plugin;

    @Default
    public void onDefault(Player player) {
        player.sendMessage(Messages.deserialize("<text>Usage: <peach>/alert <teal><message>"));
    }

    @Default
    public void onDefault(Player player, String message) {
        String location = plugin.getConfig().getString("chat.settings.alert.location", "chat");
        String title = Messages.getConfigValue("chat.settings.alert.title");
        String subtitle = Messages.getConfigValue("chat.settings.alert.subtitle");
        title = title.replace("{message}", message);
        subtitle = subtitle.replace("{message}", message);


        switch (location) {
            case "chat":
                Bukkit.broadcast(Messages.deserialize(title));
                Bukkit.broadcast(Messages.deserialize(subtitle));
                break;
            case "title":
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.showTitle(Title.title(Messages.deserialize(title), Messages.deserialize(subtitle)));
                }
                break;
            case "action_bar":
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendActionBar(Messages.deserialize(title));
                }
                break;
            default:
                player.sendMessage(Messages.deserialize("<text>Usage: <peach>/alert <teal><message>"));
                break;
        }
    }
}
