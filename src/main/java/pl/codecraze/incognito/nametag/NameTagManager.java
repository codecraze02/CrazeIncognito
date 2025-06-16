package pl.codecraze.incognito.nametag;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.codecraze.incognito.helper.ChatHelper;
import pl.codecraze.incognito.helper.PacketHelper;
import pl.codecraze.incognito.plugin.IncognitoPlugin;
import pl.codecraze.incognito.user.IncognitoUser;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class NameTagManager {



    private final IncognitoPlugin plugin;

    private final Scoreboard scoreboard = new Scoreboard();
    private final Object mutex = new Object();

    public NameTagManager(final IncognitoPlugin plugin) {
        this.plugin = plugin;

    }

    public void createNameTag(final Player player) {
        synchronized (this.mutex) {

            final String name = player.getName();
            PlayerTeam playerTeam = this.scoreboard.addPlayerTeam(name);
            playerTeam.setPlayerPrefix(Component.literal(""));
            playerTeam.setDisplayName(Component.literal(""));
            playerTeam.setPlayerSuffix(Component.literal(""));

            this.scoreboard.addPlayerToTeam(name, playerTeam);


            final ClientboundSetPlayerTeamPacket playerTeamPacket = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(
                    playerTeam, false);


            PacketHelper.sendPacket(player, playerTeamPacket);

            for (final Player requester : Bukkit.getOnlinePlayers()) {
                if (Objects.equals(requester, player)) {
                    continue;
                }
                PacketHelper.sendPacket(requester, playerTeamPacket);
                final PlayerTeam requesterTeam = this.scoreboard.getPlayerTeam(requester.getName());
                if (!Objects.isNull(requesterTeam)) {
                    PacketHelper.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(requesterTeam, false));
                }
            }

        }
    }

    public void updateNameTag(final Player player) {

        synchronized (this.mutex) {
            this.plugin.getUserManager().findUserByUniqueId(player.getUniqueId()).ifPresent(user -> {
                final PlayerTeam playerTeam = this.scoreboard.getPlayerTeam(player.getName());
                if (Objects.isNull(playerTeam)) {
                    return;
                }

                for (final Player requester : Bukkit.getOnlinePlayers()) {
                    this.plugin.getUserManager().findUserByUniqueId(player.getUniqueId()).ifPresent(requesterUser -> {


                        playerTeam.setPlayerSuffix(Component.literal(ChatHelper.colored(getValidSuffix(user, requesterUser))));

                        PacketHelper.sendPacket(requester, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true));
                    });
                }
            });
        }
    }

    public void removeNameTag(final Player player) {
        String fakeName = ((CraftPlayer) player).getProfile().getName();

        synchronized (this.mutex) {

            final PlayerTeam playerTeam = this.scoreboard.getPlayerTeam(player.getName());
            if (Objects.isNull(playerTeam)) {
                return;
            }
            this.scoreboard.removePlayerFromTeam(player.getName(), playerTeam);
            final ClientboundSetPlayerTeamPacket playerTeamPacket = ClientboundSetPlayerTeamPacket.createRemovePacket(
                    playerTeam);
            PacketHelper.sendPacket(player, playerTeamPacket);
            for (final Player requester : Bukkit.getOnlinePlayers()) {
                if (Objects.equals(player, requester)) {
                    continue;
                }
                PacketHelper.sendPacket(requester, playerTeamPacket);
                final PlayerTeam requesterTeam = this.scoreboard.getPlayerTeam(requester.getName());
                if (Objects.nonNull(requesterTeam)) {
                    PacketHelper.sendPacket(player, ClientboundSetPlayerTeamPacket.createRemovePacket(requesterTeam));
                }
            }

            this.scoreboard.removePlayerTeam(playerTeam);

        }
    }

    private String getValidSuffix(IncognitoUser user, IncognitoUser requester) {
        if (user == null) {
            return ""; // Handle null User object
        }

        StringBuilder suffix = new StringBuilder();

        if (user.isIncognitoEnabled()) {
            if (requester != null && requester.getPlayer().hasPermission("incognito.bypass")) {
                suffix.append(" &a(&c").append(user.getNickname()).append("&a) ");
            }
        }

        return ChatHelper.colored(suffix.toString());
    }
}
