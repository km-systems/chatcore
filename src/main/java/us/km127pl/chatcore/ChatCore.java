package us.km127pl.chatcore;

import co.aikar.commands.PaperCommandManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import us.km127pl.chatcore.commands.ChatCoreCommand;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload() {
        // reload config
        this.reloadConfig();

        // reload config into memory
        this.configuration = this.getConfig();
    }

    public static MiniMessage getMM() {
        return MiniMessage.miniMessage();
    }
}
