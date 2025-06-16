package pl.codecraze.incognito.helper;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/

public class ChatHelper {

    private static final Pattern HEX_PATTERN5 = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String coloreds(String text) {
        if (text == null) {
            return null;
        }

        Matcher matcher = HEX_PATTERN5.matcher(text);
        while (matcher.find()) {
            String color = text.substring(matcher.start(), matcher.end());
            text = text.replace(color, ChatColor.of(color) + "");
            matcher = HEX_PATTERN5.matcher(text);
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }


    public static String[] colored(String[] strings) {
        String[] stringArray = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            stringArray[i] = colored(strings[i]);
        }
        return stringArray;
    }

    public static String colored(String text) {
        if (text == null) {
            return null;
        }

        // First, handle the hex color codes
        Matcher matcher = HEX_PATTERN5.matcher(text);
        while (matcher.find()) {
            String color = matcher.group();
            String colorCode;
            try {
                colorCode = ChatColor.of(color).toString();
            } catch (IllegalArgumentException e) {
                // If the color code is invalid, skip the replacement
                System.err.println("Invalid hex color code: " + color);
                continue;
            }
            text = text.replace(color, colorCode);
        }

        // Then handle the standard '&' color codes
        return fixHexColor(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', text)
                .replace("%X%", "")
                .replace("%V%", "")
                .replace(">>", "»")
                .replace("<<", "«"));
    }
    public static String fixHexColor(String text) {
        return text.replaceAll("&#", "§#");
    }
    public static List<String> colored(List<String> text) {
        return (List<String>)text.stream().map(ChatHelper::colored).collect(Collectors.toList());
    }

    public static void sendMessage(CommandSender sender, String... messages) {
        sender.sendMessage((String[]) Arrays.<String>stream(messages).filter(Objects::nonNull).map(ChatHelper::colored)
                .toArray(String[]::new));
    }

    public static void sendMessage(Collection<? extends Player> players, List<?> text) {
        for (Object string : text)
            sendMessage(players, colored(String.valueOf(string)));
    }

    public static void sendMessage(Collection<? extends Player> players, String text) {
        players.forEach(player -> sendMessage((CommandSender)player, text));
    }

    public static void sendMessage(CommandSender sender, List<String> strings) {
        strings.forEach(text -> sendMessage(sender, text));
    }

    public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(colored(title), colored(subTitle), fadeIn, stay, fadeOut);
    }

    public static void sendTitle(Player player, String title, String subTitle) {
        sendTitle(player, title, subTitle, 0, 50, 25);
    }

    public static void sendActionBar(Player player, String message) {
        if (!player.isOnline())
            return;
        try {
            String mess = message.replace("_", " ");
            String s = ChatColor.translateAlternateColorCodes('&', mess);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(coloreds(mess)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}