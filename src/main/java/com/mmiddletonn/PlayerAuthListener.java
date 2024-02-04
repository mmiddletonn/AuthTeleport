package com.mmiddletonn;

import com.lenis0012.bukkit.loginsecurity.session.AuthMode;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerAuthListener implements Listener {

    private final AuthTeleport plugin;

    public PlayerAuthListener(AuthTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        JavaPlugin lsPlugin = LoginSecurity.getInstance();
        if (lsPlugin != null) {
            PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(event.getPlayer());
            AuthMode authMode = session.getAuthMode();
            Location targetLocation = getTeleportLocation(authMode);

            if (targetLocation != null) {
                event.getPlayer().teleport(targetLocation);
                plugin.getLogger().info("Teleported " + event.getPlayer().getName() + " based on their auth status: " + authMode);
            }
        }
    }

    private Location getTeleportLocation(AuthMode authMode) {
        FileConfiguration config = plugin.getConfigManager().getConfig();
        String path = "TeleportLocations." + authMode.name();
        if (!config.contains(path)) return null;

        return new Location(
                plugin.getServer().getWorld(config.getString(path + ".world")),
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z"));
    }
}