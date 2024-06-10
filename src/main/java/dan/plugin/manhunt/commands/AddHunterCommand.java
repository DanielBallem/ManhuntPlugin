package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AddHunterCommand extends BaseManhuntCommand{

    public AddHunterCommand(ManhuntGame game) {
        super(game);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        return handlePlayerCommand(
                sender,
                args,
                manhuntGame::addHunter,
                "added to the hunter team",
                label
        );
    }
}
