package pl.codecraze.incognito.helper;

import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class PacketHelper {

    public static void sendPacket(Player player, Packet<?>... packets) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Arrays.stream(packets).forEach(packet -> craftPlayer.getHandle().connection.send(packet));
    }

    public static void sendPacketToAll(Packet<?>... packets) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendPacket(onlinePlayer, packets);
        }
    }
}
