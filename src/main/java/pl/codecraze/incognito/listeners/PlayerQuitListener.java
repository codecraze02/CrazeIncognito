package pl.codecraze.incognito.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.codecraze.incognito.plugin.IncognitoPlugin;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class PlayerQuitListener implements Listener {

    private final IncognitoPlugin plugin;

    public PlayerQuitListener(final IncognitoPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        this.plugin.getNameTagManager().removeNameTag(player);

    }
}