<span style="display:flex;gap:.5em;justify-content:center;;align-items:center;">
<a href="https://modrinth.com/plugin/chatcore">
<img src="https://img.shields.io/badge/Modrinth-11111b?style=for-the-badge&logo=modrinth" alt="Modrinth" />
</a>

<a href="https://discord.gg/hQT8W434h3">
<img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="discord">
</a>

<a href="https://www.spigotmc.org/resources/placeholderapi.6245/">
<img src="https://img.shields.io/badge/Requires-PlaceholderAPI-11111b?style=for-the-badge&logo=openjdk&logoColor=white&labelColor=1e1e2e" alt="PlaceholderAPI"></a>
</span>

![ChatCore Banner](assets/chatcore%20banner%20tra.png)

Chat Core is a lightweight and easy-to-use Minecraft plugin designed to enhance your server's chat management
capabilities. Whether you're running a small community server or a large network, Chat Core provides essential features
and commands to streamline communication and improve player experience.

## Features

- [x] Chat formatting
- [x] Chat channels
- [x] Chat commands (e.g. /msg, /ignore)

## Dependencies

- PlaceholderAPI
- Paper

## Installation

1. Download the latest version of ChatCore
   from [github releases](https://github.com/km-systems/chatcore/releases/tag/0.0.1)
2. Put the jar file in your plugins folder.
3. Restart your server.
4. Configure the plugin to your liking.
5. Reload the plugin using `/chatcore reload`.

## Commands

- `/chatcore reload` - Reloads the plugin.
- `/chatcore help` - Shows the help menu.
- `/chatcore info` - Shows information about the plugin.

- `/broadcast <message>` - Broadcasts a message to the server.

- `/ignore <player>` - Ignores a player.
- `/ignore list` - Lists all ignored players.

- `/msg <player> <message>` - Sends a private message to a player.
- `/reply <message>` - Replies to the last private message received.

## Permissions

- `chatcore.manage` - Allows the user to use the `/chatcore` command.
- `chatcore.reload` - Allows the user to reload the plugin.
- `chatcore.*` - Allows the user to use all ChatCore commands.

## Configuration

The configuration file is located at `plugins/ChatCore/config.yml`. The configuration file is in YAML format.
Default configuration is in the file `src/main/resources/config.yml`.

## Issues

If you find any bugs or issues, please report them on the [issues page](https://github.com/km-systems/chatcore/issues)

## Contributing

If you would like to contribute to the project, please fork the repository and submit a pull request.

## License

This project is licensed under the GNU Affero General Public License v3.0. See the [LICENSE](LICENSE) file for details.

## Credits

- [HelpChat](https://helpch.at) for [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- [Paper](https://papermc.io) for [Paper](https://papermc.io/downloads)

## Contact

- [Discord](https://discord.gg/hQT8W434h3)
- [GitHub](https://github.com/km-systems)
