package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import dan.plugin.manhunt.utils.OptionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetRunnerCommand extends BaseManhuntCommand{

    public SetRunnerCommand(ManhuntGame game, OptionManager optionManager) {
        super(game, optionManager);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        return handlePlayerCommand(
                sender,
                args,
                manhuntGame::setRunnerTeam,
                "set as the runner",
                label
        );
    }
}
