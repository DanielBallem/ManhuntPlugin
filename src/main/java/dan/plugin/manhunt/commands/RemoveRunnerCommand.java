package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import dan.plugin.manhunt.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RemoveRunnerCommand extends BaseManhuntCommand{

    public RemoveRunnerCommand(ManhuntGame game) {
        super(game);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) {
            MessageUtils.sendError("Usage: /yourcommand", sender);
            return false;
        }
        manhuntGame.removeRunner();
        return true;
    }
}
