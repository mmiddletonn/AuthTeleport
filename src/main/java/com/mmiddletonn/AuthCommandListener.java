package com.mmiddletonn;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.session.AuthMode;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class AuthCommandListener implements Listener {

    private final AuthTeleport plugin;
    private final HashMap<UUID, Boolean> loggedInPlayers = new HashMap<>();

    public AuthCommandListener(AuthTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        Player player = event.getPlayer();
        if (message.startsWith("/login") || message.startsWith("/register")) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> checkAndTeleport(player, false), 20L); // Delay to ensure command has processed
        } else if (message.startsWith("/logout")) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> checkAndTeleport(player, true), 20L); // Delay to ensure command has processed
            // Clear the login status on logout command
            loggedInPlayers.put(player.getUniqueId(), false);
        }
    }

    private void checkAndTeleport(Player player, Boolean isLoggingOut) {
        PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
        AuthMode authMode = session.getAuthMode();
        // Check if player is already logged in
        if(isLoggingOut){
            loggedInPlayers.put(player.getUniqueId(), false); // Mark as logged out
            Location targetLocation = plugin.getConfigManager().getTeleportLocation(authMode);
            if (targetLocation != null) {
                player.teleport(targetLocation);
            }
        }else{
            if (authMode == AuthMode.AUTHENTICATED && !Boolean.TRUE.equals(loggedInPlayers.get(player.getUniqueId()))) {
                loggedInPlayers.put(player.getUniqueId(), true); // Mark as logged in
                Location targetLocation = plugin.getConfigManager().getTeleportLocation(authMode);
                if (targetLocation != null) {
                    player.teleport(targetLocation);
                    // Optionally, log or notify the player of the teleportation.
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Reset login status when a player joins
        loggedInPlayers.put(event.getPlayer().getUniqueId(), false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Clear login status when a player leaves
        loggedInPlayers.remove(event.getPlayer().getUniqueId());
    }
}
