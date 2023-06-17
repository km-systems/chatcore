package us.km127pl.chatcore.commands.utility;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.utility.Messages;

import java.util.Objects;

@CommandAlias("whois")
@CommandPermission("chatcore.whois|chatcore.*")
public class WhoisCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onDefault(Player player) {
        player.sendMessage(Messages.deserialize("<text>Usage: <peach>/whois <teal><player>"));
    }

    @Default
    public void onDefault(Player player, OnlinePlayer target) {
        // check if the player can whois
        if (target.getPlayer().hasPermission("chatcore.whois.bypass")) {
            player.sendMessage(Messages.deserialize(Messages.getConfigValue("messages.may-not-whois").replace("<player>", target.getPlayer().getName())));
            return;
        }
        player.sendMessage(Messages.deserialize("<text>Whois for <peach>" + target.getPlayer().getName() + "<text>", true));

        String uuid = target.getPlayer().getUniqueId().toString();
        player.sendMessage(Messages.deserialize("<text> - <peach>UUID: <text><hover:show_text:Click to copy the UUID><click:copy_to_clipboard:" + uuid + ">" + uuid + "</click>"));

        String ipAddress = Objects.requireNonNull(target.getPlayer().getAddress()).getAddress().getHostAddress();
        player.sendMessage(Messages.deserialize("<text> - <peach>IP: <text><hover:show_text:Click to copy the IP address><click:copy_to_clipboard:" + ipAddress + ">" + ipAddress + "</click>"));
        player.sendMessage(Messages.deserialize("<text> - <peach>Display name: <text>" + MiniMessage.miniMessage().serialize(target.getPlayer().displayName())));
        player.sendMessage(Messages.deserialize("<text> - <peach>Health: <text>" + target.getPlayer().getHealth() + "/" + target.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));

        Location location = target.getPlayer().getLocation();
        player.sendMessage(Messages.deserialize("<text> - <peach>Location: <text><hover:show_text:Click to teleport><click:run_command:/tp " + player.getName() + " " +
                location.getX() + " " +
                location.getY() + " " +
                location.getZ() + ">" + "X: " + toFixed(location.getX()) + " Y: " + toFixed(location.getY()) + " Z: " + toFixed(location.getZ()) + "</click>"));
        player.sendMessage(Messages.deserialize("<text> - <peach>World: <text>" + target.getPlayer().getWorld().getName()));
        player.sendMessage(Messages.deserialize("<text> - <peach>Game mode: <text>" + target.getPlayer().getGameMode()));
        player.sendMessage(Messages.deserialize("<text> - <peach>Operator: <text>" + target.getPlayer().isOp()));
    }

    private double toFixed(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    private double toFixed(double value) {
        return toFixed(value, 2);
    }
}
