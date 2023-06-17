package us.km127pl.chatcore.utility;


import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import us.km127pl.chatcore.ChatCore;

public enum Messages {
    TEAL("#94e2d5"),
    GREEN("#a6e3a1"),
    PEACH("#fab387"),
    TEXT("#cdd6f4"),
    RED("#f38ba8"),
    LAVENDER("#b4befe");

    public final String color;

    /**
     * Creates a new Colors enum with the specified color.
     *
     * @param color The color to use.
     */
    Messages(String color) {
        this.color = color;
    }

    /**
     * Parses a string for color codes and returns a Component with the color codes applied.
     *
     * @param string The string to parse.
     * @return The parsed string as a Component.
     * @example <pre>
     *     Component component = Colors.parseColors("<text>Hello <peach>World!");
     *     // component is now a Component with the text "Hello " in the color TEXT and the text "World!" in the color PEACH.
     *     // The color codes are not visible in the Component.
     * </pre>
     * @see Component
     */
    public static Component deserialize(String string) {
        for (Messages color : Messages.values()) {
            string = string.replaceAll("<" + color.name().toLowerCase() + ">", "<" + color.color + ">");
        }
        return MiniMessage.miniMessage().deserialize(string);
    }

    /**
     * Parses a string for color codes and returns a Component with the color codes applied.
     *
     * @param string    The string to parse.
     * @param addPrefix Whether to add the prefix to the string.
     * @return The parsed string as a Component.
     * @example <pre>
     *     Component component = Nessages.parseColors("<text>Hello <peach>World!");
     *     // component is now a Component with the text "Hello " in the color TEXT and the text "World!" in the color PEACH.
     *     // The color codes are not visible in the Component.
     * </pre>
     * @see Component
     */
    public static Component deserialize(String string, boolean addPrefix) {
        if (addPrefix) {
            return deserialize("<teal><bold>ChatCore <reset><text>" + string);
        }
        return deserialize(string);
    }

    /**
     * Gets a config value from the config.yml.
     *
     * @param path The path to the value.
     * @return The value of the specified path.
     * @example <pre>
     *     String prefix = Nessages.getConfigValue("prefix");
     *     // prefix is now the value of the "prefix" key in the config.yml.
     *     // If the key does not exist, the default value from the `resources/config.yml` will be returned.
     * </pre>
     */
    public static String getConfigValue(String path) {
        String value = ChatCore.configuration.getString(path);
        return value == null ? "not found" : value;
    }

    /**
     * Gets a config value from the config.yml.
     *
     * @param path The path to the value.
     * @return The value of the specified path.
     * @example <pre>
     *     String prefix = Nessages.getConfigValue("prefix");
     *     // prefix is now the value of the "prefix" key in the config.yml.
     *     // If the key does not exist, the default value from the `resources/config.yml` will be returned.
     * </pre>
     */
    public static Component getConfigValue(String path, boolean deserialize) {
        String value = getConfigValue(path);

        if (deserialize) {
            return deserialize(value);
        }

        return Component.text(value);
    }

    /**
     * Gets a config value from the config.yml.
     *
     * @param path The path to the value.
     * @return The value of the specified path.
     * @example <pre>
     *     String prefix = Nessages.getConfigValue("prefix");
     *     // prefix is now the value of the "prefix" key in the config.yml.
     *     // If the key does not exist, the default value from the `resources/config.yml` will be returned.
     * </pre>
     */
    public static Component getConfigValue(String path, Player player, boolean deserialize) {
        String value = getConfigValue(path);
        value = PlaceholderAPI.setPlaceholders(player, value);

        if (deserialize) {
            return deserialize(value);
        }

        return Component.text(value);
    }

    /**
     * Replaces a template string with a component
     *
     * @param template  The template to replace.
     * @param component The component to replace the template with.
     * @return The template string with the component applied.
     */
    public static Component replaceTemplate(Component template, String key, Component component) {
        return MiniMessage.miniMessage().deserialize(MiniMessage.miniMessage().serialize(template.replaceText(builder -> builder.matchLiteral(key).replacement(component))));
    }

    /**
     * Checks if a message is not ASCII
     *
     * @param message The message to check.
     * @return Whether the message is ASCII or not.
     */
    public static boolean isNonASCII(String message) {
        return !message.matches("\\A\\p{ASCII}*\\z");
    }
}
