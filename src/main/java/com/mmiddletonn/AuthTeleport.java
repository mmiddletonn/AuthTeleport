package com.mmiddletonn;

import org.bukkit.plugin.java.JavaPlugin;

public class AuthTeleport extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.configManager = new ConfigManager(this);
        this.configManager.setupConfig(); // Ensure configuration is set up
        getServer().getPluginManager().registerEvents(new PlayerAuthListener(this), this);
        getServer().getPluginManager().registerEvents(new AuthCommandListener(this), this);
        getLogger().info("AuthTeleport has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("AuthTeleport has been disabled.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}