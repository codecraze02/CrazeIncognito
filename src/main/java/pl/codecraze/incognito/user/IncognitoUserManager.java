package pl.codecraze.incognito.user;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/

public class IncognitoUserManager {
    private final Map<UUID, IncognitoUser> userMap;


    public IncognitoUserManager() {
        this.userMap = new ConcurrentHashMap<>();
    }

    public void addUser(final IncognitoUser user) {
        this.userMap.put(user.getUniqueId(), user);
    }

    public void removeUser(final IncognitoUser user) {
        this.userMap.remove(user.getUniqueId());
    }

    public IncognitoUser findUserByPlayer(final Player player) {
        return this.userMap.get(player.getUniqueId());
    }

    public Optional<IncognitoUser> findUserByUniqueId(final UUID uniqueId) {
        return Optional.ofNullable(this.userMap.get(uniqueId));
    }

    public Optional<IncognitoUser> findUserByNickname(final String nickname) {
        for (final IncognitoUser user : this.userMap.values()) {
            if (user.getNickname().equalsIgnoreCase(nickname)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    public Map<UUID, IncognitoUser> getUserMap() {
        return this.userMap;
    }
}