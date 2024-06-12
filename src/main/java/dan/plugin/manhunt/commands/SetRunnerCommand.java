package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetRunnerCommand extends BaseManhuntCommand{

    public SetRunnerCommand(ManhuntGame game) {
        super(game);
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
