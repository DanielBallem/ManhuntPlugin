package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import dan.plugin.manhunt.utils.OptionManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class StopManhuntCommand extends BaseManhuntCommand{

    public StopManhuntCommand(ManhuntGame game, OptionManager optionManager) {
        super(game, optionManager);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        manhuntGame.stopGame();
        HandlerList.unregisterAll(manhuntGame.plugin);
        sender.sendMessage(Component.text("Manhunt game has stopped"));
        return true;
    }
}
