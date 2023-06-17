package us.km127pl.chatcore;

import co.aikar.commands.PaperCommandManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.km127pl.chatcore.commands.chat.ChatCoreCommand;
import us.km127pl.chatcore.commands.chat.IgnoreCommand;
import us.km127pl.chatcore.commands.chat.MessageCommand;
import us.km127pl.chatcore.commands.chat.ReplyCommand;
import us.km127pl.chatcore.listeners.ChatListener;
import us.km127pl.chatcore.listeners.CommandPreprocessListener;
import us.km127pl.chatcore.utility.IgnoreListManager;

import java.util.HashMap;
import java.util.UUID;

public final class ChatCore extends JavaPlugin {

    public static FileConfiguration configuration;
    public static MessageCommand messageCommand;

    public static HashMap<UUID, UUID> recentMessages = new HashMap<>();


    /**
     * Gets a MiniMessage instance.
     *
     * @return A MiniMessage instance.
     */
    public static MiniMessage getMM() {
        return MiniMessage.miniMessage();
    }

    public IgnoreListManager ignoreListManager;

    @Override
    public void onEnable() {
        // save default config
        this.saveDefaultConfig();

        // load config into memory
        configuration = this.getConfig();

        // load ignore list into memory
        ignoreListManager = new IgnoreListManager(this);
        ignoreListManager.load();

        // register commands
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ChatCoreCommand());

        messageCommand = new MessageCommand(); // we're saving the instance because the CommandPreprocessListener needs it
        commandManager.registerCommand(messageCommand);
        commandManager.registerCommand(new ReplyCommand());
        commandManager.registerCommand(new IgnoreCommand());


        // listeners
        PluginManager pluginManager = getServer().getPluginManager();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            pluginManager.registerEvents(new ChatListener(this), this);
            pluginManager.registerEvents(new CommandPreprocessListener(this), this);

        } else {
            getLogger().warning("PlaceholderAPI not found! ChatCore will not work properly without it.");
        }


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
