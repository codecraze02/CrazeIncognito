package pl.codecraze.incognito.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.codecraze.incognito.helper.ChatHelper;
import pl.codecraze.incognito.helper.IncognitoHelper;
import pl.codecraze.incognito.plugin.IncognitoPlugin;
import pl.codecraze.incognito.user.IncognitoUser;

import java.util.Optional;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class IncognitoCommand implements CommandExecutor {

    private final IncognitoPlugin plugin;

    public IncognitoCommand(IncognitoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Ta komenda jest tylko dla graczy.");
            return true;
        }

        Optional<IncognitoUser> userOptional = plugin.getUserManager().findUserByUniqueId(player.getUniqueId());
        if (userOptional.isEmpty()) {
            ChatHelper.sendMessage(player, this.plugin.getConfigConfiguration().messages.PLAYER_NOT_FOUND);
            return true;
        }

        IncognitoUser user = userOptional.get();

        if (args.length == 0) {
            ChatHelper.sendMessage(player, "&cUżycie: /incognito <on|off|status>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on" -> {
                if (user.isIncognitoEnabled()) {
                    ChatHelper.sendMessage(player, this.plugin.getConfigConfiguration().messages.INCOGNITO_ALREADY_ENABLED);
                    return true;
                }
                user.setIncognitoEnabled(true);
                final String incognitoName = "UKRYTY_" + getRandomNickname();
                user.setFakeNickname(incognitoName);
                IncognitoHelper.updatePlayerProfile(user, user.getFakeNickname(), user.getFakeSkinTexture(), user.getFakeSkinTextureSignature());
                ChatHelper.sendMessage(player, this.plugin.getConfigConfiguration().messages.INCOGNITO_ENABLED);
            }
            case "off" -> {
                if (!user.isIncognitoEnabled()) {
                    ChatHelper.sendMessage(player, this.plugin.getConfigConfiguration().messages.INCOGNITO_ALREADY_DISABLED);
                    return true;
                }
                user.setIncognitoEnabled(false);
                IncognitoHelper.updatePlayerProfile(user, user.getNickname(), user.getRealSkinTexture(), user.getRealSkinTextureSignature());
                ChatHelper.sendMessage(player, this.plugin.getConfigConfiguration().messages.INCOGNITO_DISABLED);
            }
            case "status" -> {
                if (user.isIncognitoEnabled()) {
                    ChatHelper.sendMessage(player, this.plugin.getConfigConfiguration().messages.INCOGNITO_STATUS_ENABLED.replace("%nickname%", user.getFakeNickname()));
                } else {
                    ChatHelper.sendMessage(player, this.plugin.getConfigConfiguration().messages.INCOGNITO_STATUS_DISABLED.replace("%nickname%", user.getNickname()));
                }
            }
            default -> ChatHelper.sendMessage(player, "&cUżycie: /incognito <on|off|status>");
        }
        return true;
    }
    }
