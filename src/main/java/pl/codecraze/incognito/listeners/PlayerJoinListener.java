package pl.codecraze.incognito.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.codecraze.incognito.helper.ChatHelper;
import pl.codecraze.incognito.helper.IncognitoHelper;
import pl.codecraze.incognito.plugin.IncognitoPlugin;
import pl.codecraze.incognito.user.IncognitoUser;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class PlayerJoinListener implements Listener {

    private final IncognitoPlugin plugin;

    public PlayerJoinListener(final IncognitoPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if(this.plugin.getUserManager().findUserByUniqueId(player.getUniqueId()).isEmpty()) {
            this.plugin.getUserManager().addUser(new IncognitoUser(player.getUniqueId(), player.getName()));
        }

        this.plugin.getUserManager().findUserByUniqueId(player.getUniqueId()).ifPresent(user -> {
            if(user.isIncognitoEnabled()) {

                IncognitoHelper.updatePlayerProfile(user, user.getFakeNickname(), user.getFakeSkinTexture(), user.getFakeSkinTextureSignature());

                ChatHelper.sendMessage(player, this.plugin.getConfigConfiguration().messages.INCOGNITO_REFRESHED);
            }
        });
        this.plugin.getNameTagManager().createNameTag(player);



    }
}
