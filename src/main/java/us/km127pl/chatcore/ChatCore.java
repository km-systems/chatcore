package us.km127pl.chatcore;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.km127pl.chatcore.commands.admin.BroadcastCommand;
import us.km127pl.chatcore.commands.admin.SocialspyCommand;
import us.km127pl.chatcore.commands.chat.*;
import us.km127pl.chatcore.commands.utility.WhoisCommand;
import us.km127pl.chatcore.listeners.ChatListener;
import us.km127pl.chatcore.listeners.CommandPreprocessListener;
import us.km127pl.chatcore.listeners.PlayerConnectionListener;
import us.km127pl.chatcore.utility.ChatChannelManager;
import us.km127pl.chatcore.utility.IgnoreListManager;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.UUID;

public final class ChatCore extends JavaPlugin {

    private static final String UPDATE_URI = "https://api.modrinth.com/v2/project/chatcore/version";
    public static FileConfiguration configuration;
    public static MessageCommand messageCommand;
    public static HashMap<UUID, UUID> recentMessages = new HashMap<>();
    private static String USER_AGENT;
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

        // update user agent
        USER_AGENT = "ChatCore/" + this.getDescription().getVersion();

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
        commandManager.registerCommand(new SocialspyCommand());


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

        // check for updates on another thread to not block the main thread with a http request
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if (!isLatestVersion()) {
                getLogger().warning("There is a new version of ChatCore available!");
                getLogger().warning("Please update to the latest version from https://modrinth.com/plugin/chatcore");
            }
        });
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

    /**
     * Checks if the plugin is up-to-date.
     *
     * @return true if the plugin is up-to-date, false if it is not.
     * @implNote If the user has disabled update checking, this will always return true.
     */
    public boolean isLatestVersion() {
        if (!this.getConfig().getBoolean("check-for-updates"))
            return true; // if the user has disabled update checking, return true

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URL(UPDATE_URI).toURI())
                    .header("User-Agent", USER_AGENT)
                    .build();

            // send the request and get the response
            String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            JsonArray versions = JsonParser.parseString(response).getAsJsonArray();
            JsonObject latestVersion = versions.get(versions.size() - 1).getAsJsonObject();

            String latestVersionNumber = latestVersion.get("version_number").getAsString();

            // return true if the latest version is the same as the current version
            return latestVersionNumber.equals(this.getDescription().getVersion());
        } catch (Exception e) {
            // if there was an error, log it and return true
            e.printStackTrace();
            return true;
        }
    }
}
