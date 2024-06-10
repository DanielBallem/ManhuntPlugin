package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import dan.plugin.manhunt.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RunnerEnforcementCommand extends BaseManhuntCommand{

    public RunnerEnforcementCommand(ManhuntGame game) {
        super(game);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            MessageUtils.sendError("Usage: /yourcommand <true|false>", sender);
            return false;
        }

        if ("true".equalsIgnoreCase(args[0]) || "false".equalsIgnoreCase(args[0])) {
            manhuntGame.GAME_OPTION_RUNNER_ENFORCEMENT = "true".equalsIgnoreCase(args[0]);
            MessageUtils.sendConfirmation("Runner enforcement is set to " + manhuntGame.GAME_OPTION_RUNNER_ENFORCEMENT, sender);
            return true;
        } else {
            MessageUtils.sendError("Invalid argument. Usage: /yourcommand <true|false>", sender);
            return false;
        }
    }
}
