package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import static org.bukkit.Bukkit.getServer;

public class StopManhuntCommand extends BaseManhuntCommand{

    public StopManhuntCommand(ManhuntGame game) {
        super(game);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        manhuntGame.stopGame();
        HandlerList.unregisterAll(manhuntGame.plugin);
        sender.sendMessage(Component.text("Manhunt game has stopped"));
        return true;
    }
}
