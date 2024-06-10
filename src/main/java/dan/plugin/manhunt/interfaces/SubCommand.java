package dan.plugin.manhunt.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface SubCommand {
    boolean execute(CommandSender sender, Command command, String label, String[] args);
}
