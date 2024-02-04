package com.mmiddletonn;

import com.lenis0012.bukkit.loginsecurity.session.AuthMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManager {
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setupConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Could not save config.yml!");
            e.printStackTrace();
        }
    }

    public Location getTeleportLocation(AuthMode authMode) {
        FileConfiguration config = this.getConfig(); // Assuming this method fetches your plugin's config
        String path = "TeleportLocations." + authMode.name();
        if (!config.contains(path)) return null;

        return new Location(
            Bukkit.getWorld(config.getString(path + ".world")),
            config.getDouble(path + ".x"),
            config.getDouble(path + ".y"),
            config.getDouble(path + ".z")
        );
    }
}