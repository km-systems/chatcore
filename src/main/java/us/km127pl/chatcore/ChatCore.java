package us.km127pl.chatcore;

import co.aikar.commands.PaperCommandManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.km127pl.chatcore.commands.chat.*;
import us.km127pl.chatcore.commands.utility.WhoisCommand;
import us.km127pl.chatcore.listeners.ChatListener;
import us.km127pl.chatcore.listeners.CommandPreprocessListener;
import us.km127pl.chatcore.listeners.PlayerConnectionListener;
import us.km127pl.chatcore.utility.ChatChannelManager;
import us.km127pl.chatcore.utility.IgnoreListManager;

import java.util.HashMap;
import java.util.UUID;

public final class ChatCore extends JavaPlugin {

    public static FileConfiguration configuration;
    public static MessageCommand messageCommand;

    public static HashMap<UUID, UUID> recentMessages = new HashMap<>();
    public IgnoreListManager ignoreListManager;
    public ChatChannelManager chatChannelManager;

    /**
     * Gets a MiniMessage instance.
     *
     * @return A MiniMessage instance.
     */
    public static MiniMessage getMM() {
        return MiniMessage.miniMessage();
    }

    @Override
    public void onEnable() {
        // save default config
        this.saveDefaultConfig();

        // load config into memory
        configuration = this.getConfig();

        // load ignore list into memory
        ignoreListManager = new IgnoreListManager(this);
        ignoreListManager.load();

        // load chat channels
        chatChannelManager = new ChatChannelManager(this);
        chatChannelManager.load();

        // register commands
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ChatCoreCommand());

        messageCommand = new MessageCommand(); // we're saving the instance because the CommandPreprocessListener needs it
        commandManager.registerCommand(messageCommand);
        commandManager.registerCommand(new ReplyCommand());
        commandManager.registerCommand(new IgnoreCommand());
        commandManager.registerCommand(new BroadcastCommand());
        commandManager.registerCommand(new ChatChannelCommand());
        commandManager.registerCommand(new WhoisCommand());


        // listeners
        PluginManager pluginManager = getServer().getPluginManager();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            pluginManager.registerEvents(new ChatListener(this), this);
            pluginManager.registerEvents(new CommandPreprocessListener(this), this);
            pluginManager.registerEvents(new PlayerConnectionListener(this), this);

        } else {
            getLogger().warning("PlaceholderAPI not found! ChatCore will not work properly without it.");
        }

        // register @channels completion
        commandManager.getCommandCompletions().registerCompletion("channels", c -> chatChannelManager.chatChannels.keySet());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.ignoreListManager.save();
    }

    /**
     * Reloads the plugin.
     */
    public void reload() {
        // reload config
        this.reloadConfig();

        // reload config into memory
        configuration = this.getConfig();
    }
}
