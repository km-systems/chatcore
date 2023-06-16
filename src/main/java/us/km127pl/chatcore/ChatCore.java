package us.km127pl.chatcore;

import co.aikar.commands.PaperCommandManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.km127pl.chatcore.commands.ChatCoreCommand;
import us.km127pl.chatcore.listeners.ChatListener;

public final class ChatCore extends JavaPlugin {

    public FileConfiguration configuration;

    @Override
    public void onEnable() {
        // save default config
        this.saveDefaultConfig();

        // load config into memory
        this.configuration = this.getConfig();

        // register commands
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ChatCoreCommand());

        // listeners
        PluginManager pluginManager = getServer().getPluginManager();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            pluginManager.registerEvents(new ChatListener(this), this);
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
        this.configuration = this.getConfig();

    }

    /**
     * Gets a MiniMessage instance.
     * @return A MiniMessage instance.
     */
    public static MiniMessage getMM() {
        return MiniMessage.miniMessage();
    }
}
