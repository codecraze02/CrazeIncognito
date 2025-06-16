package pl.codecraze.incognito.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.codecraze.incognito.helper.ChatHelper;
import pl.codecraze.incognito.plugin.IncognitoPlugin;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class ReloadConfigCommand implements CommandExecutor {

    private final IncognitoPlugin plugin;

    public ReloadConfigCommand(IncognitoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.plugin.getConfigurationManager().loadConfig();
        ChatHelper.sendMessage(sender, this.plugin.getConfigConfiguration().messages.CONFIG_RELOADED);
        return true;
    }
}
