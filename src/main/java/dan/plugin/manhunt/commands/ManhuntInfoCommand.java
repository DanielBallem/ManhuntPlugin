package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import dan.plugin.manhunt.utils.MessageUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ManhuntInfoCommand extends BaseManhuntCommand{

    public ManhuntInfoCommand(ManhuntGame game) {
        super(game);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        List<String> playerList = manhuntGame.getHunterNames();
        MessageUtils.sendMessage("Hunters: ", sender, NamedTextColor.RED);
        MessageUtils.sendMessage("----------", sender, NamedTextColor.RED);
        playerList.forEach(name -> MessageUtils.sendMessage("- " + name, sender, NamedTextColor.RED));

        String runnerName = manhuntGame.getRunnerName();
        MessageUtils.sendMessage("Runner: " + runnerName, sender, NamedTextColor.GREEN);
        return true;
    }
}
