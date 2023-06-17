package us.km127pl.chatcore.utility;

import us.km127pl.chatcore.ChatCore;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class IgnoreListManager {
    public HashMap<UUID, ArrayList<UUID>> ignoreList;
    private ChatCore plugin;

    public IgnoreListManager(ChatCore plugin) {
        this.ignoreList = new HashMap<>();
        this.plugin = plugin;
    }

    /**
     * Saves the ignore list to disk
     * @implNote this is saved to `/plugins/ChatCore/data/ignoreList.txt`
     * @implNote the format is "uuid=uuid1,uuid2,uuid3\n"
     */
    public void save() {
        // create the folder if it doesn't exist
        File folder = new File(this.plugin.getDataFolder(), "data");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                this.plugin.getLogger().warning("Failed to create data folder!");
                return;
            }
        }
        File file = new File(this.plugin.getDataFolder(), "data/ignoreList.txt");

        // create a buffer
        StringBuilder stringBuilder = new StringBuilder();

        for (UUID uuid : this.ignoreList.keySet()) {
            stringBuilder.append(uuid.toString()).append("=");
            for (UUID ignored : this.ignoreList.get(uuid)) {
                stringBuilder.append(ignored.toString()).append(",");
            }
            stringBuilder.append("\n");
        }

        // write to disk
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.close();

            this.plugin.getLogger().info("Saved ignore list!");
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to save ignore list!", e);
        }
    }

    /**
     * Loads the ignore list from disk
     * @implNote this is loaded from `/plugins/ChatCore/data/ignoreList.txt`
     * @implNote the format is "uuid=uuid1,uuid2,uuid3\n"
     */
    public void load() {
        File file = new File(this.plugin.getDataFolder(), "data/ignoreList.txt");

        // create the file if it doesn't exist
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    this.plugin.getLogger().warning("Failed to create ignore list file!");
                    return;
                }
            } catch (IOException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to create ignore list file!", e);
                return;
            }
        }

        // read from disk
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split("=");
                if (split.length != 2) {
                    this.plugin.getLogger().warning("Invalid line in ignore list file: " + line);
                    continue;
                }

                UUID uuid = UUID.fromString(split[0]);
                String[] ignored = split[1].split(",");
                ArrayList<UUID> ignoredList = new ArrayList<>();
                for (String ignoredUUID : ignored) {
                    ignoredList.add(UUID.fromString(ignoredUUID));
                }

                this.ignoreList.put(uuid, ignoredList);
            }

            bufferedReader.close();

            this.plugin.getLogger().info("Loaded ignore list! (" + this.ignoreList.size() + " entries)");
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load ignore list!", e);
        }
    }

    /**
     * Adds a player to the ignore list
     * @param uuid the player to add
     * @param ignored the player to ignore
     */
    public void add(UUID uuid, UUID ignored) {
        if (!this.ignoreList.containsKey(uuid)) {
            this.ignoreList.put(uuid, new ArrayList<>());
        }

        this.ignoreList.get(uuid).add(ignored);
    }

    /**
     * Removes a player from the ignore list
     * @param uuid the player to remove
     * @param ignored the player to stop ignoring
     */
    public void remove(UUID uuid, UUID ignored) {
        if (!this.ignoreList.containsKey(uuid)) {
            return;
        }

        this.ignoreList.get(uuid).remove(ignored);

        // check if the player is now ignoring no one
        if (this.ignoreList.get(uuid).size() == 0) {
            this.ignoreList.remove(uuid);
        }
    }

    /**
     * Checks if a player is ignoring another player
     * @param by the player to check
     * @param ignored the player to check if they are ignoring
     * @return true if the player is ignoring the other player, false otherwise
     */
    public boolean isIgnored(UUID by, UUID ignored) {
        if (!this.ignoreList.containsKey(by)) {
            return false;
        }

        return this.ignoreList.get(by).contains(ignored);
    }
}
