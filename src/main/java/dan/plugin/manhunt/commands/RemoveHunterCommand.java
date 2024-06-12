package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import dan.plugin.manhunt.utils.OptionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RemoveHunterCommand extends BaseManhuntCommand{

    public RemoveHunterCommand(ManhuntGame game, OptionManager optionManager) {
        super(game, optionManager);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        return handlePlayerCommand(
                sender,
                args,
                manhuntGame::removeHunter,
                "removed from the hunter team",
                label
        );
    }
}
