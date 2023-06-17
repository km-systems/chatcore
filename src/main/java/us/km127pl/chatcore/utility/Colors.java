package us.km127pl.chatcore.utility;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum Colors {
    TEAL("#94e2d5"),
    GREEN("#a6e3a1"),
    PEACH("#fab387"),
    TEXT("#cdd6f4"),
    LAVENDER("#b4befe");

    public final String color;

    /**
     * Creates a new Colors enum with the specified color.
     *
     * @param color The color to use.
     */
    Colors(String color) {
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
    public static Component parseColors(String string) {
        for (Colors color : Colors.values()) {
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
     *     Component component = Colors.parseColors("<text>Hello <peach>World!");
     *     // component is now a Component with the text "Hello " in the color TEXT and the text "World!" in the color PEACH.
     *     // The color codes are not visible in the Component.
     * </pre>
     * @see Component
     */
    public static Component parseColors(String string, boolean addPrefix) {
        if (addPrefix) {
            return parseColors("<teal><bold>ChatCore <reset><text>" + string);
        }
        return parseColors(string);
    }
}
