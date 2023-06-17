package us.km127pl.chatcore;

import co.aikar.commands.PaperCommandManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.km127pl.chatcore.commands.ChatCoreCommand;
import us.km127pl.chatcore.commands.MessageCommand;
import us.km127pl.chatcore.listeners.ChatListener;
import us.km127pl.chatcore.listeners.CommandPreprocessListener;

public final class ChatCore extends JavaPlugin {

    public static FileConfiguration configuration;

    /**
     * Gets a MiniMessage instance.
     *
     * @return A MiniMessage instance.
     */
    public static MiniMessage getMM() {
        return MiniMessage.miniMessage();
    }

    public static MessageCommand messageCommand;

    @Override
    public void onEnable() {
        // save default config
        this.saveDefaultConfig();

        // load config into memory
        configuration = this.getConfig();

        // register commands
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ChatCoreCommand());
        messageCommand = new MessageCommand();
        commandManager.registerCommand(messageCommand);

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
