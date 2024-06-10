package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import dan.plugin.manhunt.utils.MessageUtils;
import dan.plugin.manhunt.interfaces.PlayerAction;
import dan.plugin.manhunt.interfaces.SubCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public abstract class BaseManhuntCommand implements SubCommand {
    protected final ManhuntGame manhuntGame;
    public BaseManhuntCommand(ManhuntGame game) {
        this.manhuntGame = game;
    }

    protected boolean handlePlayerCommand(CommandSender sender, String[] args, PlayerAction action, String successMessage, String commandLabel) {
        if (args == null || args.length != 1) {
            sender.sendMessage(Component.text("Usage: /" + commandLabel + " <playerName>"));
            return false;
        }

        Player player = getServer().getPlayer(args[0]);
        if (player == null) {
            MessageUtils.sendError("Player not found", sender);
            return false;
        }

        action.execute(player);
        MessageUtils.sendConfirmation(player.getName() + " has been " + successMessage, sender);
        return true;
    }
}
