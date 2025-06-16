package pl.codecraze.incognito.nametag;

import org.bukkit.entity.Player;
import pl.codecraze.incognito.plugin.IncognitoPlugin;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class NameTagUpdateTask implements Runnable {

    private IncognitoPlugin plugin;

    public NameTagUpdateTask(final IncognitoPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 5L, 5L);
    }

    @Override
    public void run() {
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.plugin.getNameTagManager().updateNameTag(player);
        }
    }
}
