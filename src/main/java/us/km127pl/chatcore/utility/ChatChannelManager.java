package us.km127pl.chatcore.utility;

import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ChatChannelManager {
    private final ChatCore plugin;
    public HashMap<String, ChannelInfo> chatChannels = new HashMap<>();
    public HashMap<UUID, String> playerChannels = new HashMap<>();
    public HashMap<Player, Boolean> socialSpy = new HashMap<>();
    public String defaultChannel;

    public ChatChannelManager(ChatCore chatCore) {
        this.plugin = chatCore;
    }

    /**
     * Adds a chat channel
     *
     * @param name       The name of the channel
     * @param format     The format of the channel
     * @param permission The permission required to talk in the channel
     */
    public void addChatChannel(String name, String format, String permission) {
        ChannelInfo chatInfo = new ChannelInfo();
        chatInfo.name = name;
        chatInfo.format = format;
        chatInfo.permission = permission;
        chatChannels.put(name, chatInfo);
    }

    /**
     * Adds a chat channel
     *
     * @param name        The name of the channel
     * @param format      The format of the channel
     * @param permission  The permission required to talk in the channel
     * @param isDefault   Whether the channel is the default channel
     * @param receiveFrom What other channels should this channel receive messages from
     */
    public void addChatChannel(String name, String format, String permission, boolean isDefault, int range, String... receiveFrom) {
        ChannelInfo chatInfo = new ChannelInfo();
        chatInfo.name = name;
        chatInfo.format = format;
        chatInfo.permission = permission;
        chatInfo.isDefault = isDefault;
        chatInfo.receives = receiveFrom;
        chatInfo.range = range;
        chatChannels.put(name, chatInfo);
    }

    /**
     * Removes a chat channel
     *
     * @param name The name of the channel to remove
     */
    public void removeChatChannel(String name) {
        chatChannels.remove(name);
    }

    /**
     * Loads the channels from config
     */
    public void load() {
        if (!plugin.getConfig().contains("chat.channels") || Objects.requireNonNull(plugin.getConfig().getConfigurationSection("chat.channels")).getKeys(false).size() == 0) {
            plugin.getLogger().warning("No chat channels found in config!");
            return;
        }
        for (String key : Objects.requireNonNull(plugin.getConfig().getConfigurationSection("chat.channels")).getKeys(false)) {
            String format = plugin.getConfig().getString("chat.channels." + key + ".format");
            String permission = plugin.getConfig().getString("chat.channels." + key + ".permission");
            boolean isDefault = plugin.getConfig().getBoolean("chat.channels." + key + ".default", false);
            String[] receives = plugin.getConfig().getStringList("chat.channels." + key + ".receives").toArray(new String[0]);
            int range = plugin.getConfig().getInt("chat.channels." + key + ".range", -1);
            if (format == null || permission == null) {
                plugin.getLogger().warning("Invalid chat channel: " + key);
                continue;
            }
            addChatChannel(key, format, permission, isDefault, range, receives);
            plugin.getLogger().info("Loaded chat channel: " + key);

            if (isDefault) {
                defaultChannel = key;
            }
        }
    }

    /**
     * Chat channel info
     */
    public static class ChannelInfo {
        public String name;
        public String format;
        public String permission;
        public boolean isDefault = false;
        // what other messages should this channel receive
        public String[] receives = new String[0];
        public int range = -1;
    }
}
