package pl.codecraze.incognito.user;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.codecraze.incognito.helper.IncognitoHelper;

import java.util.Optional;
import java.util.UUID;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class IncognitoUser {

    private final UUID uniqueId;
    private final String nickname;

    private String realNickname;
    private String fakeNickname;

    private String realSkinTexture;
    private String realSkinTextureSignature;

    private String fakeSkinTexture;
    private String fakeSkinTextureSignature;

    private boolean incognitoEnabled;

    public IncognitoUser(UUID uniqueId, String nickname) {
        this.uniqueId = uniqueId;
        this.nickname = nickname;

        this.realNickname = getNickname();
        this.fakeNickname = "";

        this.incognitoEnabled = false;

        this.fakeSkinTexture = IncognitoHelper.incognitoTexture;
        this.fakeSkinTextureSignature = IncognitoHelper.incognitoTextureSignature;

        Player player = getPlayer();
        if (player != null) {
            GameProfile profile = ((CraftPlayer) player).getProfile();

            Optional<Property> textureProperty = profile.getProperties().get("textures").stream().findFirst();
            textureProperty.ifPresent(property -> {
                this.realSkinTexture = property.value();
                this.realSkinTextureSignature = property.signature();
            });
        }
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getNickname() {
        return nickname;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    public String getFakeNickname() {
        return this.fakeNickname;
    }

    public String getRealNickname() {
        return this.realNickname;
    }

    public void setFakeNickname(String fakeNickname) {
        this.fakeNickname = fakeNickname;
    }

    public void setRealNickname(String realNickname) {
        this.realNickname = realNickname;
    }


    public String getRealSkinTexture() {
        return realSkinTexture;
    }

    public String getRealSkinTextureSignature() {
        return realSkinTextureSignature;
    }

    public String getFakeSkinTexture() {
        return fakeSkinTexture;
    }

    public void setFakeSkinTexture(String fakeSkinTexture) {
        this.fakeSkinTexture = fakeSkinTexture;
    }

    public String getFakeSkinTextureSignature() {
        return fakeSkinTextureSignature;
    }

    public void setFakeSkinTextureSignature(String fakeSkinTextureSignature) {
        this.fakeSkinTextureSignature = fakeSkinTextureSignature;
    }

    public boolean isIncognitoEnabled() {
        return incognitoEnabled;
    }

    public void setIncognitoEnabled(boolean incognitoEnabled) {
        this.incognitoEnabled = incognitoEnabled;
    }
}