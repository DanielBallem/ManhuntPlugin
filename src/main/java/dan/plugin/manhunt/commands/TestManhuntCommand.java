package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class TestManhuntCommand extends BaseManhuntCommand{

    public TestManhuntCommand(ManhuntGame game) {
        super(game);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        handlePlayerCommand(
                sender,
                new String[]{sender.getName()}, //player name as a test arg.
                manhuntGame::addHunter,
                "added to the hunter team",
                label
        );
        handlePlayerCommand(
                sender,
                new String[]{sender.getName()}, //player name as a test arg.
                manhuntGame::setRunner,
                "set as the runner",
                label
        );

        return new ManhuntInfoCommand(this.manhuntGame).execute(sender, command, label, args);
    }
}
