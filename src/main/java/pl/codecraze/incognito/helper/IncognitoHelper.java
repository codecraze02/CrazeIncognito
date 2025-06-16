package pl.codecraze.incognito.helper;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.CommonPlayerSpawnInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.codecraze.incognito.plugin.IncognitoPlugin;
import pl.codecraze.incognito.user.IncognitoUser;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.Collections;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class IncognitoHelper {

    public static String incognitoTexture = "eyJ0aW1lc3RhbXAiOjE1ODgwMjAxNDcxMDIsInByb2ZpbGVJZCI6IjU5YWFiNTJmMzBlMjQxY2RhN2MyMTdlYWUyZjIxOWQ2IiwicHJvZmlsZU5hbWUiOiJTclJhbWNldGkxMiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTc0YTUzYTU4NzBjOGQ3ZDY4NWE5YzA2Y2EwNzE2YTRlOTdhNjA4NjAwNzg4NWQ2NmNlZGMyNTBkNzhiZThkNCJ9fX0=";

    public static String incognitoTextureSignature = "issNEx3LijmPkOtwSzy8T8SpMRsq442A5n1hFbpF2RnLv77u5ALYFIujr+Rhw14fFSUN/xA/Nc2KHqCyGQbxuB7zz4dSXGuao1xYfY28dLZ+4aNWSVeImcWGzajnW0IskFVPCgOMg5W3MqVy/La/auWGT8YT9z/tKNgfHIf63y9D3zy9e5iU76ufmRx/nMweYi3k/k8S9nlMv9Mkw7atWYcEbi6wdhdc1lM9jlZqmNytTQJXeJ8fkizezYOruF2TnrTpeJGA1zaPu1x55bokUxK6QPWX1pebUzPgwpTu7lV/1lSjDKtNm3XXNtwdChdWB/RB7hKz4TEj2egeYBLIKygvaoHaRxP6/o27xgPl+7Q25CVNicJhB+Oen0gDXfsH3bPAnjrIky64mBIcZSNSUCu9RoSI8ZCVZTpCT+xcRTK9xvi1pupGKppkena1qVuQN57k0nvGxheKoaugZsQgDsfp2x4TNOsSc2kRzYX2RPWBpVvowSYOYYHq15briGxFbKVauz8z6wr81BWivZpkvlKxRj0SFozIkpfqdQidaAiVIMryYHAPo3AwPVk2yvqLxlkdFUTYBVR6WRPBekEOL+IwRhzcRDQsWkA9zSDg3HyXowKjzP7yEo7RgV+zAyoA6kiOwqG81ocORRZvU0uKnvd+rj00SrFjU/lFabJUk6E=";

    private static final String CHARACTERS = "!@#$%^&*()0123456789";
    private static final int LENGTH = 10;

    public static String getRandomNickname() {
        SecureRandom random = new SecureRandom();
        StringBuilder result = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }

        return result.toString();
    }

    public static void updatePlayerProfile(IncognitoUser user, String newName, String newTexture, String newSignature) {
        Player player = user.getPlayer();
        if (player == null) return;

        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = nmsPlayer.getGameProfile();


        IncognitoPlugin.getInstance().getNameTagManager().removeNameTag(player);

        updateNickname(player, newName);


        IncognitoPlugin.getInstance().getNameTagManager().createNameTag(player);

        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures",
                new Property("textures", newTexture, newSignature)
        );

        ServerLevel level = nmsPlayer.serverLevel();

        CommonPlayerSpawnInfo spawnInfo = nmsPlayer.createCommonSpawnInfo(level);
        ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(spawnInfo, ClientboundRespawnPacket.KEEP_ALL_DATA);

        nmsPlayer.connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(player.getUniqueId())));

        nmsPlayer.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.singletonList(nmsPlayer)));

        nmsPlayer.connection.send(respawn);

        nmsPlayer.onUpdateAbilities();

        nmsPlayer.connection.teleport(player.getLocation());

        nmsPlayer.resetSentInfo();

        if (IncognitoPlugin.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTask(IncognitoPlugin.getInstance(), () -> {
                PlayerList playerList = nmsPlayer.server.getPlayerList();
                playerList.sendPlayerPermissionLevel(nmsPlayer);
                playerList.sendLevelInfo(nmsPlayer, level);
                playerList.sendAllPlayerInfo(nmsPlayer);
            });
        }

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) {
                continue;
            }

            other.hidePlayer(IncognitoPlugin.getInstance(), player);

        }

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) {
                continue;
            }

            other.showPlayer(IncognitoPlugin.getInstance(), player);
        }
    }

    public static void enableIncognito(IncognitoUser user) {
        Player player = user.getPlayer();
        if (player == null) return;

        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = nmsPlayer.getGameProfile();

        String incognitoName = "UKRYTY_" + getRandomNickname();

        IncognitoPlugin.getInstance().getNameTagManager().removeNameTag(player);

        updateNickname(player, incognitoName);

        user.setFakeNickname(incognitoName);


        IncognitoPlugin.getInstance().getNameTagManager().createNameTag(player);

        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures",
                new Property("textures", user.getFakeSkinTexture(), user.getFakeSkinTextureSignature())
        );

        ServerLevel level = nmsPlayer.serverLevel();

        CommonPlayerSpawnInfo spawnInfo = nmsPlayer.createCommonSpawnInfo(level);
        ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(spawnInfo, ClientboundRespawnPacket.KEEP_ALL_DATA);

        nmsPlayer.connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(player.getUniqueId())));

        nmsPlayer.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.singletonList(nmsPlayer)));

        nmsPlayer.connection.send(respawn);

        nmsPlayer.onUpdateAbilities();

        nmsPlayer.connection.teleport(player.getLocation());

        nmsPlayer.resetSentInfo();

        if (IncognitoPlugin.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTask(IncognitoPlugin.getInstance(), () -> {
                PlayerList playerList = nmsPlayer.server.getPlayerList();
                playerList.sendPlayerPermissionLevel(nmsPlayer);
                playerList.sendLevelInfo(nmsPlayer, level);
                playerList.sendAllPlayerInfo(nmsPlayer);
            });
        }

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) {
                continue;
            }

            other.hidePlayer(IncognitoPlugin.getInstance(), player);

        }

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) {
                continue;
            }

            other.showPlayer(IncognitoPlugin.getInstance(), player);
        }




        ChatHelper.sendMessage(player, IncognitoPlugin.getInstance().getConfigConfiguration().messages.INCOGNITO_ENABLED);
        user.setIncognitoEnabled(true);
    }

    public static void disableIncognito(IncognitoUser user) {
        Player player = user.getPlayer();
        if (player == null) return;

        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = nmsPlayer.getGameProfile();

        IncognitoPlugin.getInstance().getNameTagManager().removeNameTag(player);

        updateNickname(player, user.getNickname());


        IncognitoPlugin.getInstance().getNameTagManager().createNameTag(player);

        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures",
                new Property("textures", user.getRealSkinTexture(), user.getRealSkinTextureSignature())
        );

        ServerLevel level = nmsPlayer.serverLevel();

        CommonPlayerSpawnInfo spawnInfo = nmsPlayer.createCommonSpawnInfo(level);
        ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(spawnInfo, ClientboundRespawnPacket.KEEP_ALL_DATA);

        nmsPlayer.connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(player.getUniqueId())));

        nmsPlayer.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.singletonList(nmsPlayer)));

        nmsPlayer.connection.send(respawn);

        nmsPlayer.onUpdateAbilities();

        nmsPlayer.connection.teleport(player.getLocation());

        nmsPlayer.resetSentInfo();

        if (IncognitoPlugin.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTask(IncognitoPlugin.getInstance(), () -> {
                PlayerList playerList = nmsPlayer.server.getPlayerList();
                playerList.sendPlayerPermissionLevel(nmsPlayer);
                playerList.sendLevelInfo(nmsPlayer, level);
                playerList.sendAllPlayerInfo(nmsPlayer);
            });
        }

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) {
                continue;
            }

            other.hidePlayer(IncognitoPlugin.getInstance(), player);

        }

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) {
                continue;
            }

            other.showPlayer(IncognitoPlugin.getInstance(), player);
        }

        ChatHelper.sendMessage(player, IncognitoPlugin.getInstance().getConfigConfiguration().messages.INCOGNITO_DISABLED);

        user.setIncognitoEnabled(false);
    }

    private static void updateNickname(Player player, String name) {

        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = nmsPlayer.getGameProfile();

        try {
            Field field = GameProfile.class.getDeclaredField("name");

            // Set the field accessible and update it
            field.setAccessible(true);
            field.set(profile, name);
        } catch (Exception e) {
            e.printStackTrace();
            // throw new ValidationException("Failed to update player name!", e);
        }

        player.setDisplayName(name);
        player.setPlayerListName(name);
    }
}
