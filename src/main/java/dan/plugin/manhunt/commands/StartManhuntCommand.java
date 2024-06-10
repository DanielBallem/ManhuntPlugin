package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import dan.plugin.manhunt.interfaces.SubCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static org.bukkit.Bukkit.getServer;

public class StartManhuntCommand extends BaseManhuntCommand{

    public StartManhuntCommand(ManhuntGame game) {
        super(game);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        boolean gameStarted = manhuntGame.startGame(sender);
        if (!gameStarted) {
            sender.sendMessage(Component.text("Failed to start manhunt"));
            return false;
        }
        getServer().getPluginManager().registerEvents(manhuntGame, manhuntGame.plugin);

        sender.sendMessage(Component.text("Manhunt game started"));
        return true;
    }
}
